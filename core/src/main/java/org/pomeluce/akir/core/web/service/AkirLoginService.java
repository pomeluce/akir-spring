package org.pomeluce.akir.core.web.service;

import jakarta.annotation.Resource;
import org.pomeluce.akir.common.constants.RedisKeyConstants;
import org.pomeluce.akir.common.core.redis.RedisClient;
import org.pomeluce.akir.common.exception.AkirServiceException;
import org.pomeluce.akir.common.exception.user.AkirUserCaptchaException;
import org.pomeluce.akir.common.exception.user.AkirUserCaptchaExpiredException;
import org.pomeluce.akir.common.exception.user.AkirUserPasswordNotMatchException;
import org.pomeluce.akir.core.security.context.AuthenticationContextHolder;
import org.pomeluce.akir.server.system.domain.model.LoginUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/27 23:16
 * @className : AkirLoginService
 * @description : 登录校验服务
 */
@Service
public class AkirLoginService {
    private @Resource AuthenticationManager authenticationManager;
    private @Resource AkirTokenService tokenService;
    private @Resource RedisClient redisClient;

    /**
     * 用户登录
     *
     * @param account  用户名 {@link String}
     * @param password 密码 {@link String}
     * @return 返回 String 类型的 token 信息
     */
    public String login(String account, String password) {
        Authentication authenticate;
        try {
            // 校验用户名和密码, 调用 UserDetailsService 实现类的 loadUserByUsername 方法
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(account, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new AkirUserPasswordNotMatchException();
            } else {
                throw new AkirServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContextOnExit();
        }
        // 获取登录成功的用户信息
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 返回结果
        return tokenService.accessToken(loginUser);
    }

    /**
     * 校验验证码是否正确
     *
     * @param uid  验证码 key {@link String}
     * @param code 验证码 {@link String}
     */
    public void verifyCaptcha(String uid, String code) {
        String answer = Optional.ofNullable((String) redisClient.hget(RedisKeyConstants.CAPTCHA_ANSWER_KEY, uid)).orElseThrow(AkirUserCaptchaExpiredException::new);
        redisClient.hdel(RedisKeyConstants.CAPTCHA_ANSWER_KEY, uid);
        if (!answer.equals(code)) throw new AkirUserCaptchaException();
    }
}
