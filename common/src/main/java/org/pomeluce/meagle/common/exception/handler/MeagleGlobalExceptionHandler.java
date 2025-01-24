package org.pomeluce.meagle.common.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.pomeluce.meagle.common.core.domain.HttpEntity;
import org.pomeluce.meagle.common.exception.MeagleServiceException;
import org.pomeluce.meagle.common.exception.user.MeagleUserPasswordNotMatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/28 23:42
 * @className : MeagleGlobalExceptionHandler
 * @description : 全局异常处理
 */
@RestControllerAdvice
public class MeagleGlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(MeagleGlobalExceptionHandler.class);

    /**
     * MeagleServiceException 异常处理
     *
     * @param request 请求对象 {@link HttpServletRequest}
     * @param e       异常对象 {@link MeagleServiceException}
     * @return 返回一个泛型为 String, Object 的 HttpEntity 对象
     */
    public @ExceptionHandler(MeagleServiceException.class) HttpEntity<String, Object> serviceExceptionHandler(HttpServletRequest request, MeagleServiceException e) {
        log.error("发生业务异常: {}", e.getMessage());
        return HttpEntity.instance(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * MeagleUserPasswordNotMatchException 异常处理
     *
     * @param request 请求对象 {@link HttpServletRequest}
     * @param e       异常对象 {@link MeagleUserPasswordNotMatchException}
     * @return 返回一个泛型为 String, Object 的 HttpEntity 对象
     */
    public @ExceptionHandler(MeagleUserPasswordNotMatchException.class) HttpEntity<String, Object> userPasswordNotMatchExceptionHandler(HttpServletRequest request, MeagleUserPasswordNotMatchException e) {
        log.error("用户登录发生异常: {}", e.getMessage());
        return HttpEntity.instance(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }
}
