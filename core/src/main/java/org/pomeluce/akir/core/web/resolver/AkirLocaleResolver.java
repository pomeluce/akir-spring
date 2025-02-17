package org.pomeluce.akir.core.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pomeluce.akir.common.config.AkirEnvironment;
import org.pomeluce.akir.common.utils.StringUtils;
import org.pomeluce.akir.common.utils.location.LocationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2023/10/29 16:51
 * @className : AkirLocaleResolver
 * @description : 国际化处理器
 */
public class AkirLocaleResolver extends AbstractLocaleResolver {
    private final String localeHeader = AkirEnvironment.instance.get("spring.messages.locale-header", String.class, "Accept-Language");
    private final List<Locale> supportedLocales = new ArrayList<>(4);

    /**
     * 设置支持的国际化对象列表
     *
     * @param locales 支持的国际化对象 {@link List}
     */
    public void setSupportedLocales(List<Locale> locales) {
        this.supportedLocales.clear();
        this.supportedLocales.addAll(locales);
    }

    /**
     * 获取支持的国际化对象列表
     *
     * @return 返回一个 List 类型的国际化对象
     */
    public List<Locale> getSupportedLocales() {
        return this.supportedLocales;
    }

    /**
     * 获取国际化对象
     *
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 返回一个 Locale {@link Locale} 类型的国际化对象
     */
    @Override
    public @NonNull Locale resolveLocale(@NonNull HttpServletRequest request) {
        Locale defaultLocale = this.getDefaultLocale();
        String lang = request.getHeader(localeHeader);
        if (defaultLocale != null && (StringUtils.isEmpty(lang) || !lang.matches(LocationUtils.LOCALE_REGEX))) {
            return defaultLocale;
        } else {
            Locale requestLocale = LocationUtils.resolveLocale(lang);
            List<Locale> supportedLocales = this.getSupportedLocales();
            if (!supportedLocales.isEmpty() && !supportedLocales.contains(requestLocale)) {
                Locale supportedLocale = this.findSupportedLocale(request, supportedLocales);
                return supportedLocale != null ? supportedLocale : requestLocale;
            } else {
                return requestLocale;
            }
        }
    }

    /**
     * 查找支持的国际化对象
     *
     * @param request          请求对象 {@link HttpServletRequest}
     * @param supportedLocales 支持的国际化列表 {@link List}
     * @return 返回一个 Locale {@link Locale} 类型的国际化对象
     */
    private Locale findSupportedLocale(HttpServletRequest request, List<Locale> supportedLocales) {
        Enumeration<Locale> requestLocales = request.getLocales();
        while (requestLocales.hasMoreElements()) {
            Locale locale = requestLocales.nextElement();
            if (supportedLocales.contains(locale)) {
                return locale;
            }

            for (Locale supportedLocale : supportedLocales) {
                if (supportedLocale.getLanguage().equals(locale.getLanguage())) {
                    return supportedLocale;
                }
            }
        }
        return getDefaultLocale();
    }

    /**
     * 设置国际化对象
     *
     * @param request  请求对象 {@link HttpServletRequest}
     * @param response 响应对象 {@link HttpServletResponse}
     * @param locale   国际化对象 {@link Locale}
     */
    public @Override void setLocale(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
        throw new UnsupportedOperationException(StringUtils.format("Cannot change HTTP {} header - use a different locale resolution strategy", localeHeader));
    }
}
