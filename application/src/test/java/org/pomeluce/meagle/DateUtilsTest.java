package org.pomeluce.meagle;

import org.junit.jupiter.api.Test;
import org.pomeluce.meagle.common.utils.DateUtils;

import java.util.Date;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/11/26上午12:56
 * @className : DateUtilsTest
 * @description : 日期工具测试类
 */
public class DateUtilsTest {
    public @Test void toTimestamp() {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(DateUtils.toTimestamp(currentTimeMillis));
        System.out.println(new Date(currentTimeMillis));
        System.out.println(DateUtils.toTimestamp(1701009750698L));
    }
}
