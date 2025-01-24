package org.pomeluce.meagle.common.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/9/29下午3:51
 * @className : MeagleProperty
 * @description : meagle 配置类
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.meagle")
public class MeagleProperty {
    private static MeagleProperty instance;
    private Config config;
    private Security security;
    private Token token;
    private Cors cors;
    private User user;

    /**
     * 初始化 MeagleProperty 对象
     */
    public @PostConstruct void init() {
        instance = this;
    }

    public static MeagleProperty instance() {
        return instance;
    }

    @Getter
    @Setter
    public static class Security {
        private boolean enabled = false;
        private List<String> matchers;
    }

    @Getter
    @Setter
    public static class Config {
        private boolean enableLocation;
    }

    @Getter
    @Setter
    public static class Token {
        private String algorithm;
        private String encryptKey;
        private String issuer;
        private int expireTime;
        private int refreshExpireTime;
        private String headerKey;
    }

    @Getter
    @Setter
    public static class Cors {
        private List<String> allowedOrigins;
        private List<String> allowedMethods;
    }

    @Getter
    @Setter
    public static class User {
        private Integer maxRetries;
        private Integer lockTime;
    }
}
