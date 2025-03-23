package org.pomeluce.akir.common.constants;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/22 22:14
 * @className : RedisKeyConstants
 * @description : redis key 值常量
 */
public class RedisKeyConstants {
    public static final String TOKEN_ACCESS_KEY = "jwt:access_token";
    public static final String TOKEN_REFRESH_TRANSITION = "jwt:token_refresh_transition:";

    public static final String PASSWORD_RETRIES_KEY = "user:password_retries";

    public static final String CAPTCHA_ANSWER_KEY = "captcha:answer";
}
