package org.pomeluce.akir.common.exception.base;


import org.pomeluce.akir.common.utils.StringUtils;
import org.pomeluce.akir.common.utils.spring.SpringMessage;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/22 下午10:43
 * @className : AkirBaseException
 * @description : 基础异常
 */
public class AkirBaseException extends RuntimeException {
    private String module;
    private String key;
    private String message;
    private Object[] args;

    public AkirBaseException() {
    }

    public AkirBaseException(String message) {
        this.message = message;
    }

    public AkirBaseException(String module, String key) {
        this.module = module;
        this.key = key;
    }

    public AkirBaseException(String key, Object[] args) {
        this.key = key;
        this.args = args;
    }

    public AkirBaseException(String module, String key, String message) {
        this.module = module;
        this.key = key;
        this.message = message;
    }

    public AkirBaseException(String module, String key, Object[] args) {
        this.module = module;
        this.key = key;
        this.args = args;
    }

    public AkirBaseException(String module, String key, String message, Object[] args) {
        this.module = module;
        this.key = key;
        this.message = message;
        this.args = args;
    }

    public @Override String getMessage() {
        return StringUtils.isNotBlank(key) ? SpringMessage.message(key, args) : StringUtils.isNotBlank(message) ? message : super.getMessage();
    }

    public String getModule() {
        return module;
    }

    public String getKey() {
        return key;
    }

    public Object[] getArgs() {
        return args;
    }
}
