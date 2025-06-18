package org.akir.core.security.handler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.akir.common.core.domain.HttpEntity;
import org.akir.common.enums.HttpEntityCode;
import org.akir.common.utils.spring.ServletClient;
import org.akir.common.utils.spring.SpringMessage;
import org.akir.core.web.service.AkirTokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.Optional;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/23 20:08
 * @className : LogoutSuccessHandler
 * @description : 注销成功处理器
 */
@RequiredArgsConstructor
@Configuration
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    private final AkirTokenService tokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Optional.ofNullable(tokenService.getToken(request)).map(token -> {
            try {
                return tokenService.getClaims(token).getSubject();
            } catch (ExpiredJwtException e) {
                return e.getClaims().getSubject();
            }
        }).ifPresent(username -> tokenService.delLoginUser(username));
        String message = SpringMessage.message("logout.success");
        ServletClient.responseBody(response, HttpEntity.instance(HttpEntityCode.SUCCESS.getStatus(), message), HttpStatus.OK.value());
    }
}
