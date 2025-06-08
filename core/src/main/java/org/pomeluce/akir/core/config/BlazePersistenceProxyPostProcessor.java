package org.pomeluce.akir.core.config;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.pomeluce.akir.common.core.repository.BlazePersistenceAware;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-08 12:55
 * @className : BlazePersistenceProxyPostProcessor
 * @description : BlazePersistence 拦截器
 */
@Component
public class BlazePersistenceProxyPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof CriteriaBuilderFactory && !AopUtils.isAopProxy(bean)) {
            ProxyFactory pf = new ProxyFactory(bean);
            // 保持原类型, 让 injection 依旧走 CriteriaBuilderFactory
            pf.setProxyTargetClass(true);
            pf.addInterface(BlazePersistenceAware.class);

            pf.addAdvice((MethodInterceptor) invocation -> {
                Method m = invocation.getMethod();
                if ("create".equals(m.getName()) && invocation.getArguments().length == 3 && invocation.getArguments()[2] instanceof String alias) {
                    // 先调用原 factory.create(em, cls, alias)
                    @SuppressWarnings("unchecked") CriteriaBuilder<Object> rawCb = (CriteriaBuilder<Object>) invocation.proceed();

                    // 生成带 alias 的代理
                    return Proxy.newProxyInstance(
                            CriteriaBuilder.class.getClassLoader(),
                            new Class[]{CriteriaBuilder.class, BlazePersistenceAware.class},
                            (p, method, args) -> {
                                if ("getAlias".equals(method.getName())) return alias;
                                Object res = method.invoke(rawCb, args);
                                return res == rawCb ? p : res;
                            }
                    );
                }
                // 其余方法直接透传
                return invocation.proceed();
            });
            return pf.getProxy();
        }
        return bean;
    }
}
