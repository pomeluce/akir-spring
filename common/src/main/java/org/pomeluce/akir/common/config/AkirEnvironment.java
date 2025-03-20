package org.pomeluce.akir.common.config;

import jakarta.annotation.PostConstruct;
import org.pomeluce.akir.common.utils.ObjectUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/14 13:21
 * @className : AkirEnvironment
 * @description : Environment 环境变量配置类
 */
@Configuration
public class AkirEnvironment implements EnvironmentAware {

    public static AkirEnvironment instance;
    private Environment env;

    /**
     * 初始化 AkirEnvironment 对象
     */
    private @PostConstruct void init() {
        instance = this;
    }

    /**
     * 获取 property 属性值
     *
     * @param key 待获取的 property 属性 key {@link String}
     * @return 返回一个 {@link Object} 类型的 property 值
     */
    public Object get(String key) {
        return env.getProperty(key);
    }

    /**
     * 获取 property 属性值
     *
     * @param key        待获取的 property 属性 key {@link String}
     * @param targetType 待获取的 property 类型 {@link Class<T>}
     * @param <T>        泛型参数 {@link T}
     * @return 返回一个泛型为 {@link T} 类型的 property 值
     */
    public <T> T get(String key, Class<T> targetType) {
        T value = env.getProperty(key, targetType);
        return value == null ? ObjectUtils.defaultClassValue(targetType) : value;
    }

    /**
     * 获取 property 属性值
     *
     * @param key          待获取的 property 属性 key {@link String}
     * @param targetType   待获取的 property 类型 {@link Class<T>}
     * @param defaultValue 默认值
     * @param <T>          泛型参数 {@link T}
     * @return 返回一个泛型为 {@link T} 类型的 property 值
     */
    public <T> T get(String key, Class<T> targetType, T defaultValue) {
        return env.getProperty(key, targetType, defaultValue);
    }

    public @Override void setEnvironment(@NonNull Environment environment) {
        this.env = environment;
    }
}
