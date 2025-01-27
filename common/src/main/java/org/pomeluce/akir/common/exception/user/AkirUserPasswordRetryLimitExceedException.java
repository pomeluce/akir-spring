package org.pomeluce.akir.common.exception.user;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/10 16:03
 * @className : AkirUserPasswordRetryLimitExceedException
 * @description :  用户密码重试次数超限异常
 */
public class AkirUserPasswordRetryLimitExceedException extends AkirUserException {
    public AkirUserPasswordRetryLimitExceedException(Integer maxRetries, Integer lockTime) {
        super("login.user.password.retry.limit.exceed", maxRetries, lockTime);
    }
}
