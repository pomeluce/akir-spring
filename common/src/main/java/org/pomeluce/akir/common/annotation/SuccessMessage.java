package org.pomeluce.akir.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-12 23:10
 * @className : SuccessMessage
 * @description : 请求成功响应信息注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SuccessMessage {
    String value();
}
