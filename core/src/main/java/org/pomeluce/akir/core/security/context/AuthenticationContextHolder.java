package org.pomeluce.akir.core.security.context;

import org.springframework.security.core.Authentication;

import java.util.Objects;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/29下午1:57
 * @className : AuthenticationContextHolder
 * @description : authentication 上下文管理器
 */
public class AuthenticationContextHolder {
    private static final ThreadLocal<Authentication> THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * 获取当前的 authentication 对象
     *
     * @return 返回一个 Authentication 类型的 authentication 对象
     */
    public static Authentication getContext() {
        return THREAD_LOCAL.get();
    }

    /**
     * 设置当前的 authentication 对象到上下文
     *
     * @param authentication 传入一个 Authentication 类型的 authentication 对象
     */
    public static void setContext(Authentication authentication) {
        THREAD_LOCAL.set(authentication);
    }

    /**
     * 清理 authentication 对象
     */
    public static void clearContextOnExit() {
        if (!Objects.isNull(THREAD_LOCAL.get())) {
            THREAD_LOCAL.remove();
        }
    }
}
