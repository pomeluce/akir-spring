package org.pomeluce.akir.common.exception.user;

import org.pomeluce.akir.common.exception.base.AkirBaseException;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2023/10/28下午6:03
 * @className : AkirUserException
 * @description : 用户异常
 */
public class AkirUserException extends AkirBaseException {
    public AkirUserException(String key) {
        super("user", key);
    }

    public AkirUserException(String key, String message) {
        super("user", key, message);
    }

    public AkirUserException(String key, Object... args) {
        super("user", key, args);
    }
}
