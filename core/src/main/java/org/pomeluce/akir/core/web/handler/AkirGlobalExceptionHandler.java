package org.pomeluce.akir.core.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.pomeluce.akir.common.core.domain.HttpEntity;
import org.pomeluce.akir.common.enums.HttpEntityCode;
import org.pomeluce.akir.common.exception.AkirServiceException;
import org.pomeluce.akir.common.exception.user.AkirUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/28 23:42
 * @className : AkirGlobalExceptionHandler
 * @description : 全局异常处理
 */
@RestControllerAdvice
public class AkirGlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(AkirGlobalExceptionHandler.class);

    /**
     * AkirServiceException 异常处理
     *
     * @param e 异常对象 {@link AkirServiceException}
     * @return 返回一个泛型为 String, Object 的 HttpEntity 对象
     */
    public @ExceptionHandler(AkirServiceException.class) HttpEntity<String> handleServiceException(AkirServiceException e) {
        log.error("发生业务异常: {}", e.getMessage());
        return HttpEntity.instance(HttpEntityCode.GENERAL_BUSINESS_ERROR.getStatus(), e.getMessage());
    }

    /**
     * AkirUserException 异常及子类异常处理
     *
     * @param e       异常对象 {@link AkirUserException}
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 返回一个泛型为 String, Object 的 HttpEntity 对象
     */
    public @ExceptionHandler(AkirUserException.class) HttpEntity<String> handleUserException(AkirUserException e, HttpServletRequest request) {
        log.error("请求地址: {}, 发生用户操作异常: {}", request.getRequestURI(), e.getMessage());
        return HttpEntity.instance(e.getErrorCode(), e.getMessage());
    }
}
