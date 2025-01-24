package org.pomeluce.meagle.core.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/11 01:12
 * @className : QueryDSLConfiguration
 * @description : query dsl 配置类
 */
@Configuration
public class QueryDSLConfiguration {

    public @Bean JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
