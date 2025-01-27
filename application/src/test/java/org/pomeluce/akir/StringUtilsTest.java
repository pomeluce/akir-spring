package org.pomeluce.akir;

import org.junit.jupiter.api.Test;
import org.pomeluce.akir.common.utils.StringUtils;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/11 22:14
 * @className : StringUtilsTest
 * @description : StringUtils 测试类
 */
public class StringUtilsTest {
    @Test
    void toSnakeCaseTest() {
        System.out.println(StringUtils.toSnakeCase("HTTPServer"));
        System.out.println(StringUtils.toSnakeCase("helloWorld_Case"));
    }

    @Test
    void toCamelCaseTest() {
        System.out.println(StringUtils.toCamelCase("_h_t_t_p_server"));
        System.out.println(StringUtils.toCamelCase("helloWorld_Case"));
    }
}
