package org.pomeluce.akir.common.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2023/9/29下午3:51
 * @className : AkirProperty
 * @description : akir 配置类
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.akir")
public class AkirProperty {
    private static AkirProperty instance;
    private Config config;
    private Security security;
    private Token token;
    private Cors cors;
    private User user;

    /**
     * 初始化 AkirProperty 对象
     */
    public @PostConstruct void init() {
        instance = this;
    }

    public static AkirProperty instance() {
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
        private String publicKey;
        private String privateKey;
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
