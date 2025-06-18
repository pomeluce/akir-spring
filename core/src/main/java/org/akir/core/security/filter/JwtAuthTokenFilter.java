package org.akir.core.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.akir.common.config.AkirProperty;
import org.akir.common.constants.JwtKeyConstants;
import org.akir.common.utils.spring.SecurityUtils;
import org.akir.common.utils.spring.ServletClient;
import org.akir.core.web.service.AkirTokenService;
import org.akir.server.system.domain.model.LoginUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/14 20:37
 * @className : JwtAuthTokenFilter
 * @description : security token 解析过滤器
 */
@RequiredArgsConstructor
@Configuration
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    private final AkirProperty property;
    private final AkirTokenService tokenService;
    private final ConcurrentHashMap<String, Object> LOCK_MAP = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        ServletClient.setRequestURI(request.getRequestURI());
        String token = tokenService.getToken(request);
        try {
            // 判断并获取认证信息进行验证
            if (Objects.isNull(SecurityUtils.getAuthentication()) && StringUtils.isNotBlank(token) && tokenService.checkToken(token)) {
                Optional.ofNullable(tokenService.getAuthentication(token)).ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
            }
        } catch (ExpiredJwtException e) {
            handleExpiredToken(e, token, response);
        }
        filterChain.doFilter(request, response);
    }

    private void handleExpiredToken(ExpiredJwtException e, String token, HttpServletResponse response) {
        Claims claims = e.getClaims();
        String username = claims.getSubject();
        String uid = claims.get(JwtKeyConstants.TOKEN_UID_CLAIM, String.class);
        synchronized (getUserLock(username)) {
            tokenService.getLoginUser(username).ifPresent(user -> {
                boolean isWithinRefreshTime = System.currentTimeMillis() <= user.getRefreshTime();
                if (user.getToken().equals(token) && user.getUid().equals(uid) && isWithinRefreshTime) refreshUserToken(user, token, response);
                else verifyTransitionToken(user, token, uid, username);
            });
        }
    }

    private void refreshUserToken(LoginUser user, String token, HttpServletResponse response) {
        String refreshToken = tokenService.refreshToken(user);
        tokenService.setTransitionToken(user.getUsername(), token);
        Optional.ofNullable(tokenService.getAuthentication(refreshToken)).ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
        response.setHeader(property.getToken().getRefreshHeader(), JwtKeyConstants.TOKEN_PREFIX + refreshToken);
    }

    private void verifyTransitionToken(LoginUser user, String token, String uid, String username) {
        tokenService.getTransitionToken(user.getUsername()).ifPresent(t -> {
            if (t.equals(token) && user.getUid().equals(uid)) {
                Optional.ofNullable(tokenService.getTransitionAuthentication(username, token)).ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
            }
        });
    }

    private Object getUserLock(String username) {
        return LOCK_MAP.computeIfAbsent(username, key -> new Object());
    }
}
