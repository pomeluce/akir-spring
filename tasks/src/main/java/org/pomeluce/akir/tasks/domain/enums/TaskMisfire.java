package org.pomeluce.akir.tasks.domain.enums;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/15 23:33
 * @className : TaskMisfire
 * @description : 定时任务补偿机制枚举类
 */
public enum TaskMisfire {
    /**
     * 默认
     */
    DEFAULT,
    /**
     * 错过的全部补偿, 然后正常触发
     */
    IGNORE_MISFIRES,
    /**
     * 错过的全部合并成一次并立即触发补偿一次
     */
    FIRE_AND_PROCEED,
    /**
     * 错过的不再执行
     */
    DO_NOTHING
}
