package org.pomeluce.meagle.core.web.service;

import jakarta.annotation.Resource;
import org.pomeluce.meagle.common.exception.MeagleServiceException;
import org.pomeluce.meagle.common.exception.user.MeagleUserPasswordNotMatchException;
import org.pomeluce.meagle.core.security.context.AuthenticationContextHolder;
import org.pomeluce.meagle.core.system.domain.model.LoginUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/9/27 23:16
 * @className : MeagleLoginService
 * @description : 登录校验服务
 */
@Service
public class MeagleLoginService {
    private @Resource AuthenticationManager authenticationManager;
    private @Resource MeagleTokenService tokenService;

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
                throw new MeagleUserPasswordNotMatchException();
            } else {
                throw new MeagleServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContextOnExit();
        }
        // 获取登录成功的用户信息
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 返回结果
        return tokenService.accessToken(loginUser);
    }
}
