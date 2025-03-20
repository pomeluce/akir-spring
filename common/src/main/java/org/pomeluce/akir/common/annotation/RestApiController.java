package org.pomeluce.akir.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/20下午11:14
 * @className : RestApiController
 * @description : RestApi 控制器注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface RestApiController {
    @AliasFor(annotation = RequestMapping.class) String name() default "";

    @AliasFor(annotation = RequestMapping.class) String[] value() default {};

    @AliasFor(annotation = RequestMapping.class) String[] path() default {};
}
