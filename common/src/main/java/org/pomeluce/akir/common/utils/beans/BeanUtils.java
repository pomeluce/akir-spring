package org.pomeluce.akir.common.utils.beans;

import jakarta.annotation.Nullable;
import org.pomeluce.akir.common.core.lang.Assert;
import org.pomeluce.akir.common.exception.AkirCommonUtilException;
import org.pomeluce.akir.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2023/11/11 16:53
 * @className : BeanUtils
 * @description : bean 工具类
 */
public final class BeanUtils {

    public static final Logger log = LoggerFactory.getLogger(BeanUtils.class);
    public static final PropertyDescriptor[] EMPTY_PROPERTY_DESCRIPTOR_ARRAY = new PropertyDescriptor[0];
    public static final BeanCopyOptions DEFAULT_BEAN_COPY_OPTIONS = BeanCopyOptions.instance();

    /**
     * bean 属性复制
     *
     * @param source 源对象 {@link Object}
     * @param target 目标对象 {@link Object}
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, BeanCopyOptions.instance());
    }

    /**
     * bean 属性复制
     *
     * @param source   源对象 {@link Object}
     * @param target   目标对象 {@link Object}
     * @param editable 限制类或接口 {@link Class}
     */
    public static void copyProperties(Object source, Object target, Class<?> editable) {
        copyProperties(source, target, BeanCopyOptions.instance().setEditable(editable));
    }

    /**
     * bean 属性复制
     *
     * @param source           源对象 {@link Object}
     * @param target           目标对象 {@link Object}
     * @param ignoreProperties 忽略的属性名称列表 {@link String}
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        copyProperties(source, target, BeanCopyOptions.instance().setIgnoreProperties(ignoreProperties));
    }

    /**
     * bean 属性复制
     *
     * @param source      源对象 {@link Object}
     * @param target      目标对象 {@link Object}
     * @param copyOptions 复制选项 {@link BeanCopyOptions}
     */
    public static void copyProperties(Object source, Object target, BeanCopyOptions copyOptions) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();

        if (copyOptions == null) copyOptions = DEFAULT_BEAN_COPY_OPTIONS;

        if (copyOptions.editable != null) {
            if (!copyOptions.editable.isInstance(target)) {
                String name = target.getClass().getName();
                throw new IllegalArgumentException(StringUtils.format("Target class [{}] not assignable to Editable class [{}]", name, copyOptions.editable.getName()));
            }
            actualEditable = copyOptions.editable;
        }

        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            // 判断是否忽略当前属性
            if (copyOptions.hasIgnoreProperty(targetPd.getName())) continue;
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null && readMethod.getReturnType().isAssignableFrom(writeMethod.getParameterTypes()[0])) {

                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }

                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }

                        try {
                            // 源对象属性值
                            Object srv = readMethod.invoke(source);
                            // 目标对象属性值
                            Object trv = sourcePd.getReadMethod().invoke(target);
                            // 是否忽略 null 值
                            if (copyOptions.ignoreNullValue && Objects.isNull(srv)) continue;
                            /* 是否覆盖: 覆盖则直接写入, 不覆盖则判断目标对象属性值是否为 null, 为 null 则写入, 不为 null 则跳过; */
                            if (!copyOptions.override && Objects.nonNull(trv)) continue;
                            // 写入目标属性
                            writeMethod.invoke(target, srv);
                        } catch (Throwable e) {
                            throw new AkirCommonUtilException(StringUtils.format("Could not copy property {} from source to target", targetPd.getName()), e);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取 bean 对象属性描述符
     *
     * @param beanClass    bean 字节码对象 {@link Class}
     * @param propertyName bean 对象属性名称 {@link String}
     * @return 返回一个 {@link PropertyDescriptor} 类型 bean 对象属性描述符
     */
    public static @Nullable PropertyDescriptor getPropertyDescriptor(Class<?> beanClass, String propertyName) {
        return Arrays.stream(getPropertyDescriptors(beanClass)).filter(descriptor -> descriptor.getName().equals(propertyName)).findFirst().orElse(null);
    }

    /**
     * 获取 bean 对象属性描述符数组
     *
     * @param beanClass bean 字节码对象 {@link Class}
     * @return 返回一个包含  {@link PropertyDescriptor} 类型 bean 对象属性描述符数组
     */
    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
        try {
            return Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            log.error("Failed to read bean properties", e);
            return EMPTY_PROPERTY_DESCRIPTOR_ARRAY;
        }
    }
}
