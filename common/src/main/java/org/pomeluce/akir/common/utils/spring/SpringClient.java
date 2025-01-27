package org.pomeluce.akir.common.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/28下午10:12
 * @className : SpringClient
 * @description : spring 客户端工具类
 */
@Component
public final class SpringClient implements BeanFactoryPostProcessor {
    private static ConfigurableListableBeanFactory factory;

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        factory = beanFactory;
    }

    /**
     * 根据 bean 名称获取 bean 实例
     *
     * @param name bean 名称 {@link String}
     * @param <T>  泛型 {@link T}
     * @return 返回一个 T 类型的 bean 实例
     * @throws BeansException 抛出异常 {@link BeansException}
     */
    public static @SuppressWarnings("unchecked") <T> T getBean(String name) throws BeansException {
        return (T) factory.getBean(name);
    }

    /**
     * 根据 bean 字节码对象获取 bean 实例
     *
     * @param beanClass bean 字节码对象 {@link Class}
     * @param <T>       泛型 {@link T}
     * @return 返回一个 T 类型的 bean 实例
     * @throws BeansException 抛出异常 {@link BeansException}
     */
    public static <T> T getBean(Class<T> beanClass) throws BeansException {
        return factory.getBean(beanClass);
    }

    /**
     * 判断 bean 是否存在
     *
     * @param name bean 名称
     * @return 返回一个 boolean 类型的判断结果
     */
    public static boolean containsBean(String name) {
        return factory.containsBean(name);
    }
}
