package org.akir.common.enums;

import lombok.Getter;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/21 20:52
 * @className : HttpEntityCode
 * @description : 请求响应结果枚举项
 */
@Getter
public enum HttpEntityCode {
    SUCCESS("A0000", "request.success"),
    ERROR("E0000", "request.failed"),

    RESOURCE_REQUIRE_AUTHENTICATION("E1001", "resource.require.authentication"),
    RESOURCE_REQUIRE_AUTHORIZATION("E1002", "resource.require.authorization"),
    RESOURCE_NOT_EXIST("E1003", "resource.not.exist"),
    RESOURCE_ACCESS_FAILED("E1004", "resource.access.failed"),

    LOGIN_USER_NOT_EXISTS("E2001", "login.user.not.exists"),
    LOGIN_USER_PASSWORD_NOT_MATCH("E2002", "login.user.password.not.match"),
    LOGIN_USER_PASSWORD_RETRY_LIMIT_EXCEED("E2003", "login.user.password.retry.limit.exceed"),
    LOGIN_USER_DELETED("E2004", "login.user.deleted"),
    LOGIN_USER_DISABLED("E2005", "login.user.disabled"),
    LOGIN_USER_CAPTCHA_FAILED("E2006", "login.user.captcha.failed"),
    LOGIN_USER_CAPTCHA_EXPIRED("E2007", "login.user.captcha.expired"),

    GENERAL_BUSINESS_ERROR("E3000");

    private final String status;
    private String content;

    HttpEntityCode(String status) {
        this.status = status;
    }

    HttpEntityCode(String status, String content) {
        this.status = status;
        this.content = content;
    }
}
