package org.akir.common.core.lang;

import jakarta.annotation.Nullable;

import java.util.function.Supplier;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/11/11下午9:38
 * @className : Assert
 * @description : 断言
 */
public class Assert {
    /**
     * 断言对象不为空
     * <br>
     * <p>如果对象为 null 则抛出 {@link  IllegalArgumentException} 异常</p>
     * <br>
     *
     * @param object  待断言的对象 {@link Object}
     * @param message 异常信息 {@link String}
     */
    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言对象不为空
     * <br>
     * <p>如果对象为 null 则抛出 {@link  IllegalArgumentException} 异常</p>
     * <br>
     *
     * @param object 待断言的对象 {@link Object}
     * @param messageSupplier 消息供应接口 {@link Supplier}
     */
    public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * 安全获取消息文本
     *
     * @param messageSupplier 消息供应接口 {@link Supplier}
     * @return 返回一个 String 类型的消息文本
     */
    private static @Nullable String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return messageSupplier != null ? messageSupplier.get() : null;
    }
}
