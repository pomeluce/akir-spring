package org.pomeluce.akir.common.utils.spring;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/29下午1:46
 * @className : SecurityUtils
 * @description : security 工具类
 */
public class SecurityUtils {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /**
     * 生成加密密码
     *
     * @param password 密码 {@link String}
     * @return 返回一个 {@link String} 类型的加密密码
     */
    public static String encoderPassword(String password) {
        return ENCODER.encode(password);
    }

    /**
     * 获取 authentication 验证对象
     *
     * @return 返回一个 {@link Authentication} 类型的验证对象
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 删除上下文登录信息
     */
    public static void clearContext() {
        SecurityContextHolder.clearContext();
    }

    /**
     * 校验密码
     *
     * @param rowPassword     原始密码 {@link String}
     * @param encodedPassword 加密密码 {@link String}
     * @return 返回一个 {@link Boolean} 类型的判断结果
     */
    public static boolean matches(String rowPassword, String encodedPassword) {
        return ENCODER.matches(rowPassword, encodedPassword);
    }
}
