package org.pomeluce.akir.common.exception;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/28 23:22
 * @className : AkirServiceException
 * @description : service 通用异常类
 */
public class AkirServiceException extends RuntimeException {
    private String message;

    public AkirServiceException() {
    }

    public AkirServiceException(String message) {
        this.message = message;
    }

    public @Override String getMessage() {
        return message;
    }

    public AkirServiceException setMessage(String message) {
        this.message = message;
        return this;
    }
}
