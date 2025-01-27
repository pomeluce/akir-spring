package org.pomeluce.akir.common.utils.spring;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/28下午10:31
 * @className : SpringMessage
 * @description : spring 信息国际化
 */
public class SpringMessage {

    /**
     * 获取国际化信息
     *
     * @param key    消息 key {@link String}
     * @param params 参数 {@link Object}
     * @return 返回一个 {@link String} 类型的国际化信息
     */
    public static String message(String key, Object... params) {
        return SpringClient.getBean(MessageSource.class).getMessage(key, params, LocaleContextHolder.getLocale());
    }

    /**
     * 获取国际化信息
     *
     * @param key     消息 key {@link String}
     * @param request 请求对象 {@link HttpServletRequest}
     * @param params  参数 {@link Object}
     * @return 返回一个 {@link String} 类型的国际化信息
     */
    public static String message(String key, HttpServletRequest request, Object... params) {
        return SpringClient.getBean(MessageSource.class).getMessage(key, params, SpringClient.getBean(LocaleResolver.class).resolveLocale(request));
    }
}
