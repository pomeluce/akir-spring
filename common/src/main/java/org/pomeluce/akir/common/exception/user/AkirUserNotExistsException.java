package org.pomeluce.akir.common.exception.user;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/28 23:07
 * @className : AkirUserNotExistsException
 * @description : 用户不存在异常
 */
public class AkirUserNotExistsException extends AkirUserException {
    public AkirUserNotExistsException(Object... args) {
        super("login.user.not.exists", args);
    }
}
