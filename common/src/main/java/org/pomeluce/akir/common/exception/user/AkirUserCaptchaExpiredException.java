package org.pomeluce.akir.common.exception.user;

import org.pomeluce.akir.common.enums.HttpEntityCode;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/23 22:00
 * @className : AkirUserCaptchaExpiredException
 * @description : 验证码失效异常类
 */
public class AkirUserCaptchaExpiredException extends AkirUserException {
    public AkirUserCaptchaExpiredException() {
        super(HttpEntityCode.LOGIN_USER_CAPTCHA_EXPIRED.getStatus(), HttpEntityCode.LOGIN_USER_CAPTCHA_EXPIRED.getContent());
    }
}
