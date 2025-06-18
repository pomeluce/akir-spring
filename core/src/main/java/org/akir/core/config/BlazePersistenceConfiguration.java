package org.akir.core.config;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/11 11:24
 * @className : BlazePersistenceConfiguration
 * @description : blaze persistence 配置
 */
@Configuration(proxyBeanMethods = false)
public class BlazePersistenceConfiguration {
    private @PersistenceUnit EntityManagerFactory entityManagerFactory;

    public @Bean CriteriaBuilderFactory criteriaBuilderFactory() {
        return Criteria.getDefault().createCriteriaBuilderFactory(entityManagerFactory);
    }
}
