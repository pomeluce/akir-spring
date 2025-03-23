package org.pomeluce.akir.common.exception.user;

import org.pomeluce.akir.common.enums.HttpEntityCode;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/10 16:03
 * @className : AkirUserPasswordRetryLimitExceedException
 * @description :  用户密码重试次数超限异常
 */
public class AkirUserPasswordRetryLimitExceedException extends AkirUserException {
    public AkirUserPasswordRetryLimitExceedException(Integer maxRetries, Long lockTime) {
        super(HttpEntityCode.LOGIN_USER_PASSWORD_RETRY_LIMIT_EXCEED.getStatus(), HttpEntityCode.LOGIN_USER_PASSWORD_RETRY_LIMIT_EXCEED.getContent(), maxRetries, lockTime);
    }
}
