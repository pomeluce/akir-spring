package org.pomeluce.meagle.common.core.controller;

import org.pomeluce.meagle.common.core.domain.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/9/29 11:18
 * @className : BaseController
 * @description : web 通用控制器
 */
public class BaseController {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected <R> HttpEntity<R, Object> success(String message) {
        return success(message, null);
    }

    protected <R> HttpEntity<R, Object> success(String message, R data) {
        return HttpEntity.instance(HttpStatus.OK.value(), message, data);
    }

    protected <R> HttpEntity<R, Object> error(String message) {
        return error(message, null);
    }

    protected <R> HttpEntity<R, Object> error(String message, R data) {
        return HttpEntity.instance(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, data);
    }
}
