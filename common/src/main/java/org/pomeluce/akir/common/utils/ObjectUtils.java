package org.pomeluce.akir.common.utils;


import org.pomeluce.akir.common.exception.AkirCommonUtilException;
import org.pomeluce.akir.common.utils.beans.BeanCopyOptions;
import org.pomeluce.akir.common.utils.beans.BeanUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2023/11/11 15:46
 * @className : ObjectUtils
 * @description : 对象工具类
 */
public final class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {
    /**
     * 拷贝方法
     *
     * @param source 源对象 {@link T}
     * @param <T>    泛型
     * @return 返回一个 {@link T} 类型的拷贝对象
     */
    public static <T> T clone(T source) {
        @SuppressWarnings("unchecked") T t = defaultClassValue((Class<T>) source.getClass());
        BeanUtils.copyProperties(source, t);
        return t;
    }

    /**
     * 深拷贝方法
     * <br>
     * <strong>该方法仅适用于实现了 {@link Serializable} 接口的对象</strong>
     * <br>
     *
     * @param source 源对象 {@link T}
     * @param <T>    泛型
     * @return 返回一个实现了 {@link Serializable} 接口的 {@link T} 类型的深拷贝对象
     */
    public static @SuppressWarnings("unchecked") <T extends Serializable> T deepClone(T source) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(source);
            out.flush();

            try (InputStream is = new ByteArrayInputStream(byteOut.toByteArray())) {
                ObjectInputStream in = new ObjectInputStream(is);
                return (T) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new AkirCommonUtilException(e);
        }
    }

    /**
     * 获取基本类型默认值
     *
     * @param targetType 要获取默认值的字节码类型 {@link Class<T>}
     * @param <T>        泛型参数
     * @return 返回一个泛型为 {@link  T} 类型的默认值
     */
    private static @SuppressWarnings("unchecked") <T> T defaultBaseValue(Class<T> targetType) {
        return switch (targetType.getSimpleName()) {
            case "byte" -> (T) Byte.valueOf((byte) 0);
            case "short" -> (T) Short.valueOf((short) 0);
            case "int" -> (T) Integer.valueOf(0);
            case "long" -> (T) Long.valueOf(0);
            case "float" -> (T) Float.valueOf(0);
            case "double" -> (T) Double.valueOf(0);
            case "boolean" -> (T) Boolean.FALSE;
            case "char" -> (T) Character.valueOf('\u0000');
            default -> null;
        };
    }

    /**
     * 根据字节码类型获取默认值
     *
     * @param targetType 要获取默认值的字节码类型 {@link Class<T>}
     * @param <T>        泛型参数
     * @return 返回一个泛型为 {@link  T} 类型的默认值
     */
    public static @SuppressWarnings("unchecked") <T> T defaultClassValue(Class<T> targetType) {
        T value = targetType.getSimpleName().endsWith("[]") ? (T) Array.newInstance(targetType.getComponentType(), 0) : defaultBaseValue(targetType);

        if (value == null) {
            Constructor<T> constructor = (Constructor<T>) Arrays.stream(targetType.getDeclaredConstructors()).findFirst().orElseThrow(
                    () -> new AkirCommonUtilException("The current class has no constructor")
            );
            try {
                constructor.setAccessible(true);
                value = constructor.newInstance(Arrays.stream(constructor.getParameterTypes()).map(ObjectUtils::defaultBaseValue).toArray());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new AkirCommonUtilException(e);
            }
        }
        return value;
    }

    /**
     * 对象合并
     * <br>
     * <p> 该方法仅适用于实现了 {@link Serializable} 接口的对象 </p>
     *
     * @param source 源对象 {@link T}
     * @param target 目标对象 {@link T}
     * @param <T>    泛型
     * @return 返回一个实现了 {@link Serializable} 接口的 {@link T} 类型的对象的合并结果
     */
    public static <T extends Serializable> T merge(T source, T target) {
        return merge(source, target, false);
    }

    /**
     * 对象合并
     * <br>
     * <p> 该方法仅适用于实现了 {@link Serializable} 接口的对象 </p>
     *
     * @param source     源对象 {@link T}
     * @param target     目标对象 {@link T}
     * @param isOverride 是否覆盖 {@link Boolean}
     * @param <T>        泛型
     * @return 返回一个实现了 {@link Serializable} 接口的 {@link T} 类型的对象的合并结果
     */
    public static <T extends Serializable> T merge(T source, T target, boolean isOverride) {
        T t = deepClone(target);
        BeanUtils.copyProperties(source, t, BeanCopyOptions.instance().ignoreNullValue().setOverride(isOverride));
        return t;
    }

    /**
     * 判断对象是否为空, 如果为空返回默认值, 否则返回原值
     *
     * @param value        要判断的值 {@link Object}
     * @param defaultValue 默认值 {@link T}
     * @param <T>          泛型
     * @return 返回一个 {@link T} 类型的值
     */
    public static @SuppressWarnings("unchecked") <T> T isEmpty(Object value, T defaultValue) {
        return value == null ? defaultValue : (T) value;
    }
}
