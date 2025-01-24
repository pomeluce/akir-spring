package org.pomeluce.meagle.common.config;

import org.pomeluce.meagle.common.annotation.RestApiController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/20 23:40
 * @className : MeagleRestApiConfig
 * @description : RestApiController 配置类
 */
@Configuration
public class MeagleRestApiConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", config -> config.isAnnotationPresent(RestApiController.class));
    }
}
