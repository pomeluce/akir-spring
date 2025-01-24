package org.pomeluce.meagle.core.web.service;

import jakarta.annotation.Resource;
import org.pomeluce.meagle.common.config.MeagleProperty;
import org.pomeluce.meagle.common.core.redis.RedisClient;
import org.pomeluce.meagle.common.enums.CacheKey;
import org.pomeluce.meagle.common.exception.MeagleServiceException;
import org.pomeluce.meagle.common.exception.user.MeagleUserPasswordNotMatchException;
import org.pomeluce.meagle.common.exception.user.MeagleUserPasswordRetryLimitExceedException;
import org.pomeluce.meagle.common.utils.ObjectUtils;
import org.pomeluce.meagle.common.utils.spring.SecurityUtils;
import org.pomeluce.meagle.common.utils.spring.SpringMessage;
import org.pomeluce.meagle.core.security.context.AuthenticationContextHolder;
import org.pomeluce.meagle.core.system.domain.entity.User;
import org.pomeluce.meagle.core.system.domain.model.LoginUser;
import org.pomeluce.meagle.core.system.repository.SystemUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/9/27 11:17
 * @className : MeagleUserDetailsService
 * @description : 用户验证处理
 */
@Service
public class MeagleUserDetailsService implements UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(MeagleUserDetailsService.class);

    private @Resource MeagleProperty property;
    private @Resource SystemUserRepository repository;
    private @Resource RedisClient redisClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByAccount(username).orElseThrow(() -> {
            log.error("当前登录用户:{} 不存在", username);
            return new MeagleServiceException(SpringMessage.message("login.user.not.exists", username));
        });

        /*
         校验用户状态
          1. 判断用户是否被删除
          2. 判断用户是否被禁用
          3. 判断用户是否被锁定
         */
        switch (user.getStatus()) {
            case DELETE -> {
                log.warn("当前登录用户:{} 已被删除", user.getAccount());
                throw new MeagleServiceException(SpringMessage.message("login.user.deleted", user.getAccount()));
            }
            case DISABLED -> {
                log.warn("当前登录用户:{} 已被禁用", user.getAccount());
                throw new MeagleServiceException(SpringMessage.message("login.user.disabled", user.getAccount()));
            }
            case ENABLED -> isLock(user);
        }

        return new LoginUser(user);
    }

    /**
     * 校验登录用户
     * 1. 校验错误次数
     * 2. 校验登录密码
     * 3. 校验登录次数
     *
     * @param user 登录用户 {@link User}
     */
    private void isLock(User user) {
        Authentication context = AuthenticationContextHolder.getContext();
        String account = context.getName();
        Integer maxRetries = property.getUser().getMaxRetries();
        Integer lockTime = property.getUser().getLockTime();
        Integer count = ObjectUtils.isEmpty(redisClient.hget(CacheKey.PASSWORD_RETRIES_KEY.value(), account), 0);

        // 校验错误次数
        if (count >= maxRetries) {
            log.warn("当前登录用户:{} 密码错误错误次数超过限制, 已被锁定 {} 分钟", user.getAccount(), lockTime);
            throw new MeagleUserPasswordRetryLimitExceedException(maxRetries, lockTime);
        }

        // 校验登录密码
        if (!SecurityUtils.matches(context.getCredentials().toString(), user.getPassword())) {
            redisClient.hset(CacheKey.PASSWORD_RETRIES_KEY.value(), account, ++count, lockTime, TimeUnit.MINUTES);
            log.warn("当前登录用户:{} 密码错误", user.getAccount());
            throw new MeagleUserPasswordNotMatchException();
        }
        // 登录成功, 清空错误次数
        else {
            redisClient.hdel(CacheKey.PASSWORD_RETRIES_KEY.value(), account);
        }
    }
}
