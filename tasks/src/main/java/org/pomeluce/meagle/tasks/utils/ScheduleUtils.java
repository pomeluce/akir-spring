package org.pomeluce.meagle.tasks.utils;

import org.pomeluce.meagle.common.constants.ScheduleKey;
import org.pomeluce.meagle.common.enums.ScheduleExceptionCode;
import org.pomeluce.meagle.common.exception.tasks.MeagleScheduleException;
import org.pomeluce.meagle.common.utils.StringUtils;
import org.pomeluce.meagle.tasks.domain.entity.Task;
import org.pomeluce.meagle.tasks.domain.enums.TaskStatus;
import org.quartz.*;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/9 21:08
 * @className : ScheduleUtils
 * @description : 任务调度器工具类
 */
public class ScheduleUtils {
    public static JobKey getJobKey(Long id, String group) {
        return JobKey.jobKey(StringUtils.format("{}_{}", ScheduleKey.TASK_KEY, id), group);
    }

    public static TriggerKey getTriggerKey(Long id, String group) {
        return TriggerKey.triggerKey(StringUtils.format("{}_{}", ScheduleKey.TASK_KEY, id), group);
    }

    /**
     * 创建任务
     *
     * @param scheduler 调度器 {@link  Scheduler}
     * @param context   任务信息 {@link  Task}
     * @throws SchedulerException 抛出一个 {@link SchedulerException} 类型的异常
     */
    public static void create(Scheduler scheduler, Task context) throws SchedulerException, MeagleScheduleException {
        Long id = context.getId();
        String group = context.getGroupId();
        JobKey jobKey = getJobKey(id, group);

        // 任务详情
        JobDetail detail = JobBuilder.newJob(QuartzJobExecution.class).storeDurably().withIdentity(jobKey).build();

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = withMisfireHandling(CronScheduleBuilder.cronSchedule(context.getCronExpression()), context);

        // 使用 cron 表达式生成 trigger 对象
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(id, group)).withSchedule(cronScheduleBuilder).build();

        // 保存任务信息
        detail.getJobDataMap().put(ScheduleKey.CONTEXT_KEY, context);

        // 判断任务是否存在
        if (scheduler.checkExists(jobKey)) scheduler.deleteJob(jobKey);

        // 创建任务
        scheduler.scheduleJob(detail, trigger);

        // 判断是否暂停任务
        if (context.getStatus().equals(TaskStatus.PAUSE)) scheduler.pauseJob(jobKey);
    }

    /**
     * 暂停任务
     *
     * @param scheduler 调度器 {@link  Scheduler}
     * @param context   任务信息 {@link  Task}
     */
    public static void pause(Scheduler scheduler, Task context) throws SchedulerException {
        JobKey key = getJobKey(context.getId(), context.getGroupId());
        scheduler.pauseJob(key);
    }

    /**
     * 恢复任务
     *
     * @param scheduler 调度器 {@link  Scheduler}
     * @param context   任务信息 {@link  Task}
     */
    public static void resume(Scheduler scheduler, Task context) throws SchedulerException {
        JobKey key = getJobKey(context.getId(), context.getGroupId());
        scheduler.resumeJob(key);
    }

    /**
     * 删除任务
     *
     * @param scheduler 调度器 {@link  Scheduler}
     * @param context   任务信息 {@link  Task}
     */
    public static void delete(Scheduler scheduler, Task context) throws SchedulerException {
        JobKey key = getJobKey(context.getId(), context.getGroupId());
        scheduler.deleteJob(key);
    }

    /**
     * 立即执行一次任务
     *
     * @param scheduler 调度器 {@link  Scheduler}
     * @param name      任务名称 {@link  Task#getName()}
     */
    public static void once(Scheduler scheduler, String name) {
    }

    /**
     * 修改任务
     *
     * @param scheduler 调度器 {@link  Scheduler}
     * @param context   任务信息 {@link  Task}
     */
    public static void modify(Scheduler scheduler, Task context) {
    }

    public static CronScheduleBuilder withMisfireHandling(CronScheduleBuilder builder, Task context) throws MeagleScheduleException {
        try {
            return switch (context.getMisfirePolicy()) {
                case DEFAULT -> builder;
                case IGNORE_MISFIRES -> builder.withMisfireHandlingInstructionIgnoreMisfires();
                case FIRE_AND_PROCEED -> builder.withMisfireHandlingInstructionFireAndProceed();
                case DO_NOTHING -> builder.withMisfireHandlingInstructionDoNothing();
            };
        } catch (IllegalArgumentException e) {
            throw new MeagleScheduleException(StringUtils.format("the schedule misfire policy '{}' cannot be used in cron schedule build", context.getMisfirePolicy()), ScheduleExceptionCode.MISFIRE_ERROR, e);
        }
    }
}
