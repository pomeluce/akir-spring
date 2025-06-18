package org.akir.common.exception.user;

import org.akir.common.enums.HttpEntityCode;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/28 23:07
 * @className : AkirUserNotExistsException
 * @description : 用户不存在异常
 */
public class AkirUserNotExistsException extends AkirUserException {
    public AkirUserNotExistsException(Object... args) {
        super(HttpEntityCode.LOGIN_USER_NOT_EXISTS.getStatus(), HttpEntityCode.LOGIN_USER_NOT_EXISTS.getContent(), args);
    }
}
