package org.pomeluce.meagle.common.exception.user;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/28 23:07
 * @className : MeagleUserNotExistsException
 * @description : 用户不存在异常
 */
public class MeagleUserNotExistsException extends MeagleUserException {
    public MeagleUserNotExistsException(Object... args) {
        super("login.user.not.exists", args);
    }
}
