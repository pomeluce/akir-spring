package org.pomeluce.akir.common.utils;

import org.springframework.scheduling.support.CronExpression;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2024/8/9 21:16
 * @className : CronUtils
 * @description : Cron 表达式工具类
 */
public class CronUtils {
    /**
     * 判断 cron 表达式是否正确
     *
     * @param expression cron 表达式 {@link  String}
     * @return 返回一个 {@link Boolean} 类型的判断结果
     */
    public static boolean isValidExpression(String expression) {
        return CronExpression.isValidExpression(expression);
    }
}
