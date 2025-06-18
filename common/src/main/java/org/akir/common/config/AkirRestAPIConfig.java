package org.akir.common.config;

import lombok.RequiredArgsConstructor;
import org.akir.common.annotation.RestAPIController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/20 23:40
 * @className : AkirRestApiConfig
 * @description : RestApiController 配置类
 */
@RequiredArgsConstructor
@Configuration
public class AkirRestAPIConfig implements WebMvcConfigurer {
    private final AkirProperty property;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(property.getConfig().getApiPrefix(), config -> config.isAnnotationPresent(RestAPIController.class));
    }
}
