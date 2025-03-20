package org.pomeluce.akir.core.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pomeluce.akir.common.core.domain.HttpEntity;
import org.pomeluce.akir.common.utils.spring.ServletClient;
import org.pomeluce.akir.common.utils.spring.SpringMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/21 18:06
 * @className : AuthEntryPointHandler
 * @description : 匿名请求处理器
 */
@Component
public class AuthEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        HttpStatus status = Objects.requireNonNullElse(HttpStatus.resolve(response.getStatus()), HttpStatus.UNAUTHORIZED);
        String message = SpringMessage.message(switch (status) {
            case HttpStatus.OK, HttpStatus.UNAUTHORIZED -> {
                status = HttpStatus.UNAUTHORIZED;
                yield "resource.require.authentication";
            }
            case HttpStatus.FORBIDDEN -> "resource.require.authorization";
            case HttpStatus.NOT_FOUND -> "resource.not.exist";
            default -> "resource.access.failed";
        }, request, ServletClient.getRequestURI());
        ServletClient.responseBody(response, HttpEntity.instance(status.value(), message));
    }
}
