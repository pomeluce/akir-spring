package org.akir.common.text;


import org.akir.common.utils.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/30下午9:55
 * @className : Parser
 * @description : 字符串解析
 */
public final class Parser implements Serializable {
    private static final String ESCAPE = "\\";
    private static final String ANTI_ESCAPE = "\\\\";
    private static final TextConvert TEXT_CONVERT = TextConvert.instance();

    public String parser(final String openTag, final String closeTag, final String text, final Object... args) {
        if (Objects.isNull(args) || args.length == 0) return text;
        if (StringUtils.isBlank(text)) return "";

        int offset = 0;
        final int length = text.length();
        final StringBuilder buffer = new StringBuilder(length + args.length * 16);

        for (int i = 0; i < args.length; i++) {
            int index = text.indexOf(openTag + closeTag, offset);
            boolean isEscape = text.startsWith(ESCAPE, index - ESCAPE.length());
            if (index == 0 || (index > 0 && !isEscape) || (index > 1 && text.startsWith(ANTI_ESCAPE, index - ANTI_ESCAPE.length()))) {
                buffer.append(text, offset, isEscape ? index - 1 : index);
                buffer.append(TEXT_CONVERT.toString(args[i]));
                offset = index + (openTag + closeTag).length();
            } else if (index > 0) {
                buffer.append(text, offset, index);
                buffer.append(openTag);
                offset = index + openTag.length();
                i--;
            } else {
                return offset == 0 ? text : buffer.append(text, offset, length).toString();
            }
        }
        return buffer.append(text, offset, length).toString();
    }
}
