package org.pomeluce.akir.common.exception.tasks;


import org.pomeluce.akir.common.enums.ScheduleExceptionCode;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/9 21:12
 * @className : AkirScheduleException
 * @description : 定时任务异常
 */
public class AkirScheduleException extends Exception {
    private final ScheduleExceptionCode key;

    public AkirScheduleException(String message, ScheduleExceptionCode key) {
        super(message);
        this.key = key;
    }

    public AkirScheduleException(String message, ScheduleExceptionCode key, Exception exception) {
        super(message, exception);
        this.key = key;
    }

    public ScheduleExceptionCode getKey() {
        return key;
    }
}
