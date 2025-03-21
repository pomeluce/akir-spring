package org.pomeluce.akir.common.exception.user;

import org.pomeluce.akir.common.enums.HttpEntityCode;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/21 21:53
 * @className : AkirUserDeletedException
 * @description : 用户已删除异常
 */
public class AkirUserDeletedException extends AkirUserException {
    public AkirUserDeletedException() {
        super(HttpEntityCode.LOGIN_USER_DELETED.getStatus(), HttpEntityCode.LOGIN_USER_DELETED.getContent());
    }
}
