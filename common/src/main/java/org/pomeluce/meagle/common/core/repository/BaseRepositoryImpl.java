package org.pomeluce.meagle.common.core.repository;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.pomeluce.meagle.common.core.page.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/11 21:19
 * @className : BaseRepositoryImpl
 * @description : 通用持久层抽象实现
 */
public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    private final EntityManager entityManager;
    protected final BlazeJPAQueryFactory factory;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager, BlazeJPAQueryFactory factory) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
        this.factory = factory;
    }

    @Override
    public void entityClear() {
        entityManager.clear();
    }

    @Override
    public void entityDetach(T entity) {
        entityManager.detach(entity);
    }

    @Override
    public <K> Optional<List<K>> fetchPage(BlazeJPAQuery<K> query, Pageable pageable) {
        return Optional.ofNullable(pageable).map(page -> {
            page.getDslOrderBy().map(query::orderBy);
            page.getDefaultOrder().map(query::orderBy);
            return query.fetchPage(page.offset(), page.getPageSize());
        });
    }

    public Query nativeQuery(String sql) {
        return entityManager.createNativeQuery(sql);
    }

    public <K> Query nativeQuery(String sql, Class<K> cls) {
        return entityManager.createNativeQuery(sql, cls);
    }
}
