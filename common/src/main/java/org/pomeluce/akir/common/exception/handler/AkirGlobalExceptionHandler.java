package org.pomeluce.akir.common.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.pomeluce.akir.common.core.domain.HttpEntity;
import org.pomeluce.akir.common.exception.AkirServiceException;
import org.pomeluce.akir.common.exception.user.AkirUserPasswordNotMatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
     * @param request 请求对象 {@link HttpServletRequest}
     * @param e       异常对象 {@link AkirServiceException}
     * @return 返回一个泛型为 String, Object 的 HttpEntity 对象
     */
    public @ExceptionHandler(AkirServiceException.class) HttpEntity<String, Object> serviceExceptionHandler(HttpServletRequest request, AkirServiceException e) {
        log.error("发生业务异常: {}", e.getMessage());
        return HttpEntity.instance(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * AkirUserPasswordNotMatchException 异常处理
     *
     * @param request 请求对象 {@link HttpServletRequest}
     * @param e       异常对象 {@link AkirUserPasswordNotMatchException}
     * @return 返回一个泛型为 String, Object 的 HttpEntity 对象
     */
    public @ExceptionHandler(AkirUserPasswordNotMatchException.class) HttpEntity<String, Object> userPasswordNotMatchExceptionHandler(HttpServletRequest request, AkirUserPasswordNotMatchException e) {
        log.error("用户登录发生异常: {}", e.getMessage());
        return HttpEntity.instance(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }
}
