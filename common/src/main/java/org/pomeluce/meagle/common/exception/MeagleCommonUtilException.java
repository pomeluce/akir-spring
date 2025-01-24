package org.pomeluce.meagle.common.exception;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/9/29 15:29
 * @className : MeagleCommonUtilException
 * @description : 工具类异常
 */
public class MeagleCommonUtilException extends RuntimeException {
    public MeagleCommonUtilException(Throwable cause) {
        super(cause);
    }

    public MeagleCommonUtilException(String message) {
        super(message);
    }

    public MeagleCommonUtilException(String message, Throwable cause) {
        super(message, cause);
    }
}
