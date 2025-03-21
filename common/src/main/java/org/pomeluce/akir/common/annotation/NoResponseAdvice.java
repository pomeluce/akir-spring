package org.pomeluce.akir.common.annotation;

import java.lang.annotation.*;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/21 22:17
 * @className : NoResponseAdvice
 * @description : 跳过响应统一拦截器
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoResponseAdvice {
}
