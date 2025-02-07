package org.pomeluce.akir.common.enums;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2023/9/29 19:23
 * @className : CacheKey
 * @description : 缓存 key 枚举类
 */
public enum CacheKey {
    TOKEN_LOGIN_USER_KEY("login_user"),
    TOKEN_ACCESS_KEY("access_token"),
    TOKEN_PREFIX_KEY("Bearer "),
    TOKEN_BLACK_KEY("token_blacklist"),
    TOKEN_EXPIRED_TIME_KEY("expired_time"),
    TOKEN_REFRESH_TIME_KEY("refresh_time"),

    PASSWORD_RETRIES_KEY("password_retries");

    private final String key;

    CacheKey(String key) {
        this.key = key;
    }

    public String value() {
        return key;
    }
}
