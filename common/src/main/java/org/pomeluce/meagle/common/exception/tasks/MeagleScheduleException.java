package org.pomeluce.meagle.common.exception.tasks;


import org.pomeluce.meagle.common.enums.ScheduleExceptionCode;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/9 21:12
 * @className : MeagleScheduleException
 * @description : 定时任务异常
 */
public class MeagleScheduleException extends Exception {
    private final ScheduleExceptionCode key;

    public MeagleScheduleException(String message, ScheduleExceptionCode key) {
        super(message);
        this.key = key;
    }

    public MeagleScheduleException(String message, ScheduleExceptionCode key, Exception exception) {
        super(message, exception);
        this.key = key;
    }

    public ScheduleExceptionCode getKey() {
        return key;
    }
}
