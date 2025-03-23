package org.pomeluce.akir.common.exception.user;

import org.pomeluce.akir.common.enums.HttpEntityCode;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/23 21:48
 * @className : AkirUserCaptchaException
 * @description : 验证码异常类
 */
public class AkirUserCaptchaException extends AkirUserException {
    public AkirUserCaptchaException() {
        super(HttpEntityCode.LOGIN_USER_CAPTCHA_FAILED.getStatus(), HttpEntityCode.LOGIN_USER_CAPTCHA_FAILED.getContent());
    }
}
