package org.pomeluce.akir.common.utils.id;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/28下午8:21
 * @className : IdGenerator
 * @description : id 生成工具类
 */
public class IdGenerator {

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
     * @param simplify 是否去除横线 {@link String}
     * @return 返回一个 string 类型的 uuid
     */
    public static String randomUUID(boolean simplify) {
        String uuid = UUID.randomUUID().toString();
        return simplify ? uuid.replaceAll("-", "") : uuid;
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
