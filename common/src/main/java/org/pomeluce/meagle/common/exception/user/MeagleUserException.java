package org.pomeluce.meagle.common.exception.user;

import org.pomeluce.meagle.common.exception.base.MeagleBaseException;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/28下午6:03
 * @className : MeagleUserException
 * @description : 用户异常
 */
public class MeagleUserException extends MeagleBaseException {
    public MeagleUserException(String key) {
        super("user", key);
    }

    public MeagleUserException(String key, String message) {
        super("user", key, message);
    }

    public MeagleUserException(String key, Object... args) {
        super("user", key, args);
    }
}
