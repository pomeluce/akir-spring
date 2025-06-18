package org.akir.core.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akir.common.config.AkirProperty;
import org.akir.common.constants.RedisKeyConstants;
import org.akir.common.core.redis.RedisClient;
import org.akir.common.exception.user.*;
import org.akir.common.utils.ObjectUtils;
import org.akir.common.utils.spring.SecurityUtils;
import org.akir.core.security.context.AuthenticationContextHolder;
import org.akir.server.system.domain.entity.User;
import org.akir.server.system.domain.model.LoginUser;
import org.akir.server.system.repository.SystemUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/27 11:17
 * @className : AkirUserDetailsService
 * @description : 用户验证处理
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AkirUserDetailsService implements UserDetailsService {
    private final AkirProperty property;
    private final SystemUserRepository repository;
    private final RedisClient redisClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByAccount(username).orElseThrow(() -> {
            log.error("当前登录用户:{} 不存在", username);
            return new AkirUserNotExistsException(username);
        });

        /*
         校验用户状态
          1. 判断用户是否被删除
          2. 判断用户是否被禁用
          3. 判断用户是否被锁定
         */
        switch (user.getStatus()) {
            case DELETED -> {
                log.warn("当前登录用户:{} 已被删除", user.getAccount());
                throw new AkirUserDeletedException();
            }
            case DISABLED -> {
                log.warn("当前登录用户:{} 已被禁用", user.getAccount());
                throw new AkirUserDisabledException();
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
        Long lockTime = property.getUser().getLockTime();
        Integer count = ObjectUtils.isEmpty(redisClient.hget(RedisKeyConstants.PASSWORD_RETRIES_KEY, account), 0);

        // 校验错误次数
        if (count >= maxRetries) {
            log.warn("当前登录用户:{} 密码错误错误次数超过限制, 已被锁定 {} 分钟", user.getAccount(), lockTime);
            throw new AkirUserPasswordRetryLimitExceedException(maxRetries, lockTime);
        }

        // 校验登录密码
        if (!SecurityUtils.matches(context.getCredentials().toString(), user.getPassword())) {
            redisClient.hset(RedisKeyConstants.PASSWORD_RETRIES_KEY, account, ++count, lockTime, TimeUnit.MINUTES);
            log.warn("当前登录用户:{} 密码错误", user.getAccount());
            throw new AkirUserPasswordNotMatchException();
        }
        // 登录成功, 清空错误次数
        else {
            redisClient.hdel(RedisKeyConstants.PASSWORD_RETRIES_KEY, account);
        }
    }
}
