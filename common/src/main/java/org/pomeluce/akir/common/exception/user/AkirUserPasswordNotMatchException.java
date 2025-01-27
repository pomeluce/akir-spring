package org.pomeluce.akir.common.exception.user;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/28 下午5:52
 * @className : AkirUserPasswordNotMatchException
 * @description : 用户密码不匹配异常
 */
public class AkirUserPasswordNotMatchException extends AkirUserException {
    public AkirUserPasswordNotMatchException() {
        super("login.user.password.not.match");
    }

    public AkirUserPasswordNotMatchException(String message) {
        super("login.user.password.not.match", message);
    }

}
