package org.akir.core.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.akir.common.core.domain.HttpEntity;
import org.akir.common.enums.HttpEntityCode;
import org.akir.common.utils.spring.ServletClient;
import org.akir.common.utils.spring.SpringMessage;
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
        HttpEntityCode code = switch (status) {
            case HttpStatus.OK, HttpStatus.UNAUTHORIZED -> {
                status = HttpStatus.UNAUTHORIZED;
                yield HttpEntityCode.RESOURCE_REQUIRE_AUTHENTICATION;
            }
            case HttpStatus.FORBIDDEN -> HttpEntityCode.RESOURCE_REQUIRE_AUTHORIZATION;
            case HttpStatus.NOT_FOUND -> HttpEntityCode.RESOURCE_NOT_EXIST;
            default -> HttpEntityCode.RESOURCE_ACCESS_FAILED;
        };
        String message = SpringMessage.message(code.getContent(), request, ServletClient.getRequestURI());
        ServletClient.responseBody(response, HttpEntity.instance(code.getStatus(), message), status.value());
    }
}
