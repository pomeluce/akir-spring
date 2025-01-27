package org.pomeluce.akir.core.config;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/11 11:24
 * @className : BlazePersistenceConfiguration
 * @description : blaze persistence 配置
 */
@Configuration(proxyBeanMethods = false)
public class BlazePersistenceConfiguration {

    public @Bean BlazeJPAQueryFactory createBlazeJPAQuery(EntityManager entityManager, EntityManagerFactory entityManagerFactory) {
        return new BlazeJPAQueryFactory(entityManager, Criteria.getDefault().createCriteriaBuilderFactory(entityManagerFactory));
    }
}
