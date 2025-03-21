package org.pomeluce.akir.common.exception.user;

import lombok.Getter;
import org.pomeluce.akir.common.exception.base.AkirBaseException;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/28下午6:03
 * @className : AkirUserException
 * @description : 用户异常
 */
@Getter
public class AkirUserException extends AkirBaseException {
    private final String errorCode;

    public AkirUserException(String errorCode, String key) {
        super("user", key);
        this.errorCode = errorCode;
    }

    public AkirUserException(String errorCode, String key, String message) {
        super("user", key, message);
        this.errorCode = errorCode;
    }

    public AkirUserException(String errorCode, String key, Object... args) {
        super("user", key, args);
        this.errorCode = errorCode;
    }
}
