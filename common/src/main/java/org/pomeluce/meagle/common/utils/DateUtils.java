package org.pomeluce.meagle.common.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/11/26上午12:32
 * @className : DateUtils
 * @description : 日期工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_ZONED_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");

    public static Date toDate(String text) {
        return null;
    }

    public static Timestamp toTimestamp(String text) {
        return null;
    }

    /**
     * 将毫秒数转换为 {@link Timestamp}
     *
     * @param mills 毫秒数 {@link Long}
     * @return 返回一个 {@link Timestamp} 类型的时间对象
     */
    public static Timestamp toTimestamp(long mills) {
        return Timestamp.from(Instant.ofEpochMilli(mills));
    }

    public static LocalDate toLocalDate(String text) {
        return null;
    }

    public static LocalDate toLocalDateTime(String text) {
        return null;
    }

    public static OffsetDateTime toOffsetDateTime(String text) {
        return null;
    }
}
