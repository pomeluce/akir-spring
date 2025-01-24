package org.pomeluce.meagle.common.utils;


import org.pomeluce.meagle.common.text.Parser;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/29下午9:16
 * @className : StringUtils
 * @description : string 工具类
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final char UNDER_LINE = '_';

    /**
     * 格式化字符串
     *
     * @param text 模板字符串 {@link String}
     * @param args 参数 {@link Object}...
     * @return 返回一个 {@link String} 类型的格式化字符串
     */
    public static String format(String text, Object... args) {
        return new Parser().parser("{", "}", text, args);
    }

    /**
     * 驼峰转下划线命令
     *
     * @param value 要转换的字符串 {@link String}
     * @return 返回一个 {@link String} 类型的转换结果
     */
    public static String toSnakeCase(String value) {
        if (isEmpty(value)) return null;
        int length = value.length();
        StringBuilder builder = new StringBuilder(length + 2);
        char pre = 0;
        for (int i = 0; i < length; i++) {
            char ch = value.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (pre != UNDER_LINE) builder.append(UNDER_LINE);
                builder.append(Character.toLowerCase(ch));
            } else builder.append(ch);
            pre = ch;
        }
        return builder.toString();
    }

    /**
     * 下划线转驼峰命令
     *
     * @param value 要转换的字符串 {@link String}
     * @return 返回一个 {@link String} 类型的转换结果
     */
    public static String toCamelCase(String value) {
        if (isEmpty(value)) return null;

        int length = value.length();
        StringBuilder builder = new StringBuilder(length);

        boolean afterUnderline = false;

        for (int i = 0; i < length; ++i) {
            char ch = value.charAt(i);
            if (ch == UNDER_LINE) afterUnderline = true;
            else if (afterUnderline) {
                builder.append(Character.toUpperCase(ch));
                afterUnderline = false;
            } else builder.append(ch);
        }

        return builder.toString();
    }
}
