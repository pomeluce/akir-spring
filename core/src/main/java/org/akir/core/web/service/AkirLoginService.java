package org.akir.core.web.service;

import com.pig4cloud.captcha.ArithmeticCaptcha;
import com.pig4cloud.captcha.ChineseCaptcha;
import com.pig4cloud.captcha.SpecCaptcha;
import lombok.RequiredArgsConstructor;
import org.akir.common.constants.ExpiredTimeConstants;
import org.akir.common.constants.RedisKeyConstants;
import org.akir.common.core.redis.RedisClient;
import org.akir.common.exception.AkirServiceException;
import org.akir.common.exception.user.AkirUserCaptchaException;
import org.akir.common.exception.user.AkirUserCaptchaExpiredException;
import org.akir.common.exception.user.AkirUserPasswordNotMatchException;
import org.akir.common.utils.id.IdGenerator;
import org.akir.core.security.context.AuthenticationContextHolder;
import org.akir.server.system.domain.enums.CaptchaType;
import org.akir.server.system.domain.model.Captcha;
import org.akir.server.system.domain.model.LoginUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/27 23:16
 * @className : AkirLoginService
 * @description : 登录校验服务
 */
@RequiredArgsConstructor
@Service
public class AkirLoginService {
    private final AuthenticationManager authenticationManager;
    private final AkirTokenService tokenService;
    private final RedisClient redisClient;

    /**
     * 用户登录
     *
     * @param account  用户名 {@link String}
     * @param password 密码 {@link String}
     * @return 返回 {@link String} 类型的 token 信息
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
        String answer = Optional.ofNullable((String) redisClient.get(RedisKeyConstants.CAPTCHA_ANSWER_KEY + uid)).orElseThrow(AkirUserCaptchaExpiredException::new);
        redisClient.delete(RedisKeyConstants.CAPTCHA_ANSWER_KEY + uid);
        if (!answer.equals(code)) throw new AkirUserCaptchaException();
    }

    /**
     * 生成验证码
     *
     * @param type 验证码类型 {@link CaptchaType}
     * @return 返回一个 {@link Captcha} 类型的验证码对象
     */
    public Captcha generateCaptcha(CaptchaType type) {
        String uid = IdGenerator.randomUUID(true);
        return switch (type) {
            case DEFAULT -> {
                SpecCaptcha cap = new SpecCaptcha();
                cap.setLen(6);
                String answer = cap.text();
                redisClient.set(RedisKeyConstants.CAPTCHA_ANSWER_KEY + uid, answer, ExpiredTimeConstants.CAPTCHA, TimeUnit.MILLISECONDS);
                yield new Captcha(uid, cap.toBase64());
            }
            case MATH -> {
                // 生成算术验证码
                ArithmeticCaptcha cap = new ArithmeticCaptcha();
                cap.supportAlgorithmSign(4);
                cap.setDifficulty(100);
                String answer = cap.text();
                redisClient.set(RedisKeyConstants.CAPTCHA_ANSWER_KEY + uid, answer, ExpiredTimeConstants.CAPTCHA, TimeUnit.MILLISECONDS);
                yield new Captcha(uid, cap.toBase64());
            }
            case CHINESE -> {
                ChineseCaptcha cap = new ChineseCaptcha();
                cap.setLen(5);
                String answer = cap.text();
                redisClient.set(RedisKeyConstants.CAPTCHA_ANSWER_KEY + uid, answer, ExpiredTimeConstants.CAPTCHA, TimeUnit.MILLISECONDS);
                yield new Captcha(uid, cap.toBase64());
            }
        };
    }
}
