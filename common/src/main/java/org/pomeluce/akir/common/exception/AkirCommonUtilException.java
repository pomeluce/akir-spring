package org.pomeluce.akir.common.exception;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/29 15:29
 * @className : AkirCommonUtilException
 * @description : 工具类异常
 */
public class AkirCommonUtilException extends RuntimeException {
    public AkirCommonUtilException(Throwable cause) {
        super(cause);
    }

    public AkirCommonUtilException(String message) {
        super(message);
    }

    public AkirCommonUtilException(String message, Throwable cause) {
        super(message, cause);
    }
}
