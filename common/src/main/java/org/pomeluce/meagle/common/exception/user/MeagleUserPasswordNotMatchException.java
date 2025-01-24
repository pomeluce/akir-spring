package org.pomeluce.meagle.common.exception.user;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/28 下午5:52
 * @className : MeagleUserPasswordNotMatchException
 * @description : 用户密码不匹配异常
 */
public class MeagleUserPasswordNotMatchException extends MeagleUserException {
    public MeagleUserPasswordNotMatchException() {
        super("login.user.password.not.match");
    }

    public MeagleUserPasswordNotMatchException(String message) {
        super("login.user.password.not.match", message);
    }

}
