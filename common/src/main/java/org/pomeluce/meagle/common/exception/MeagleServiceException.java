package org.pomeluce.meagle.common.exception;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/28 23:22
 * @className : MeagleServiceException
 * @description : service 通用异常类
 */
public class MeagleServiceException extends RuntimeException {
    private String message;

    public MeagleServiceException() {
    }

    public MeagleServiceException(String message) {
        this.message = message;
    }

    public @Override String getMessage() {
        return message;
    }

    public MeagleServiceException setMessage(String message) {
        this.message = message;
        return this;
    }
}
