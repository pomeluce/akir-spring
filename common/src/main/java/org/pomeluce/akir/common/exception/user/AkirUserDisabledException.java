package org.pomeluce.akir.common.exception.user;

import org.pomeluce.akir.common.enums.HttpEntityCode;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/21 21:57
 * @className : AkirUserDisabledException
 * @description : 用户禁用异常
 */
public class AkirUserDisabledException extends AkirUserException {
    public AkirUserDisabledException() {
        super(HttpEntityCode.LOGIN_USER_DISABLED.getStatus(), HttpEntityCode.LOGIN_USER_DISABLED.getContent());
    }
}
