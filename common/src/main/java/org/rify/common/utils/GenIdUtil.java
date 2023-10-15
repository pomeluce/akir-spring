package org.rify.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/9/28下午8:21
 * @className : GenIdUtil
 * @description : id 生成工具类
 */
public class GenIdUtil {

    /**
     * 随机生成 uuid
     *
     * @return 返回一个 string 类型的 uuid
     */
    public static String randomUUID() {
        return randomUUID(false);
    }

    /**
     * 随机生成 uuid
     *
     * @param isSimple 是否简化 {@link String}
     * @return 返回一个 string 类型的 uuid
     */
    public static String randomUUID(boolean isSimple) {
        String uuid = UUID.randomUUID().toString();
        return isSimple ? uuid.replaceAll("-", "") : uuid;
    }


    /**
     * 获取当前时间戳
     *
     * @return 返回一个 long 类型的时间戳
     */
    public static Long timestamp() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}
