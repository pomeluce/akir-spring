package org.pomeluce.meagle.core.web.config;

import org.pomeluce.meagle.common.config.MeagleEnvironment;
import org.pomeluce.meagle.common.utils.location.LocationUtils;
import org.pomeluce.meagle.core.web.resolver.MeagleLocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/29 18:25
 * @className : WebMvcConfigurer
 * @description : web mvc 配置
 */
@Configuration
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    /**
     * 注入国际化处理器
     *
     * @return 返回一个 LocaleResolver 类型的 locale 解析器
     */
    public @Bean LocaleResolver localeResolver() {
        @SuppressWarnings("unchecked") List<String> locales = MeagleEnvironment.instance.get("spring.messages.support-locale", List.class, Collections.singletonList("zh_CN"));
        List<Locale> supportLocales = locales.stream().filter(locale -> locale.matches(LocationUtils.LOCALE_REGEX)).map(LocationUtils::resolveLocale).distinct().toList();
        MeagleLocaleResolver resolver = new MeagleLocaleResolver();
        resolver.setSupportedLocales(supportLocales);
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return resolver;
    }
}
