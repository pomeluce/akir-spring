package org.pomeluce.akir.core.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.pomeluce.akir.common.config.AkirProperty;
import org.pomeluce.akir.core.security.filter.JwtAuthTokenFilter;
import org.pomeluce.akir.core.security.handler.AuthEntryPointHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/27 下午11:07
 * @className : SecurityConfiguration
 * @description : security 核心配置类
 */
@Configuration
public class SecurityConfiguration {
    private @Resource AuthenticationConfiguration authenticationConfiguration;
    private @Resource AuthEntryPointHandler entryPointHandler;
    private @Resource JwtAuthTokenFilter authTokenFilter;
    private @Resource AkirProperty property;
    private String[] matchers = {"/**"};

    private @PostConstruct void init() {
        Boolean enabled = property.getSecurity().isEnabled();
        matchers = Boolean.TRUE.equals(enabled) ? property.getSecurity().getMatchers().toArray(String[]::new) : matchers;
    }

    /**
     * 生成 BCryptPasswordEncoder 对象
     *
     * @return 返回一个 BCryptPasswordEncoder 加密对象
     */
    public @Bean BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager 认证管理器
     *
     * @return 返回一个认证管理器
     * @throws Exception 抛出异常
     */
    public @Bean AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 配置跨域资源共享
     *
     * @return 返回一个 CorsConfigurationSource 跨域资源共享 (CORS) 的类
     */
    public @Bean CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(property.getCors().getAllowedOrigins());
        configuration.setAllowedMethods(property.getCors().getAllowedMethods());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * security 核心配置
     *
     * @param http http 安全配置对象 {@link HttpSecurity}
     * @return 返回一个 HttpSecurity 对象
     * @throws Exception 抛出异常
     */
    public @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 关闭 csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 指定 session 的创建策略, 不使用 session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 跨域配置
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 进行认证请求的配置
                .authorizeHttpRequests(auth -> auth
                        // 放行接口
                        .requestMatchers(matchers)
                        // 登录接口有无认证都可以访问
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                // 认证过滤器
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 匿名访问和未授权访问处理器
                .exceptionHandling(handling -> handling.authenticationEntryPoint(entryPointHandler))
                .build();
    }
}
