package org.akir.core.web.handler;

import jakarta.annotation.Nullable;
import org.akir.common.annotation.NoResponseAdvice;
import org.akir.common.annotation.SuccessMessage;
import org.akir.common.core.domain.HttpEntity;
import org.akir.common.enums.HttpEntityCode;
import org.akir.common.utils.JacksonUtils;
import org.akir.common.utils.ObjectUtils;
import org.akir.common.utils.spring.SpringMessage;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;


/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/21 22:14
 * @className : AkirHttpEntityAdvice
 * @description : 请求响应数据统一处理器
 */
@RestControllerAdvice
public class AkirHttpEntityAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, @Nullable Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.getDeclaringClass().isAnnotationPresent(NoResponseAdvice.class)) return false;
        return !Objects.requireNonNull(returnType.getMethod()).isAnnotationPresent(NoResponseAdvice.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, @Nullable MethodParameter returnType, @Nullable MediaType selectedContentType, @Nullable Class<? extends HttpMessageConverter<?>> selectedConverterType, @Nullable ServerHttpRequest request, @Nullable ServerHttpResponse response) {
        String messageKey = HttpEntityCode.SUCCESS.getContent();
        if (returnType != null) {
            SuccessMessage sma = returnType.getMethodAnnotation(SuccessMessage.class);
            if (ObjectUtils.isNotEmpty(sma)) messageKey = sma.value();
        }
        if (body instanceof HttpEntity<?>) return body;
        HttpEntity<Object> result = HttpEntity.instance(HttpEntityCode.SUCCESS.getStatus(), SpringMessage.message(messageKey), body);
        if (body instanceof String) return JacksonUtils.toJson(result);
        return result;
    }
}
