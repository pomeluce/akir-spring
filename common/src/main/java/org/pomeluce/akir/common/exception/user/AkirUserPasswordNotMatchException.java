package org.pomeluce.akir.common.exception.user;

import org.pomeluce.akir.common.enums.HttpEntityCode;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/28 下午5:52
 * @className : AkirUserPasswordNotMatchException
 * @description : 用户密码不匹配异常
 */
public class AkirUserPasswordNotMatchException extends AkirUserException {
    public AkirUserPasswordNotMatchException() {
        super(HttpEntityCode.LOGIN_USER_PASSWORD_NOT_MATCH.getStatus(), HttpEntityCode.LOGIN_USER_PASSWORD_NOT_MATCH.getContent());
    }

    public AkirUserPasswordNotMatchException(String message) {
        super(HttpEntityCode.LOGIN_USER_PASSWORD_NOT_MATCH.getStatus(), HttpEntityCode.LOGIN_USER_PASSWORD_NOT_MATCH.getContent(), message);
    }

}
