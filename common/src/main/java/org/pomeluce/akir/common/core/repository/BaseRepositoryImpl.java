package org.pomeluce.akir.common.core.repository;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.pomeluce.akir.common.core.page.Pageable;
import org.pomeluce.akir.common.utils.StringUtils;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/11 21:19
 * @className : BaseRepositoryImpl
 * @description : 通用持久层抽象实现
 */
public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    protected @PersistenceContext EntityManager em;
    protected final CriteriaBuilderFactory factory;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em, CriteriaBuilderFactory factory) {
        super(domainClass, em);
        this.em = em;
        this.factory = factory;
    }

    @Override
    public void entityClear() {
        em.clear();
    }

    @Override
    public void entityDetach(T entity) {
        em.detach(entity);
    }

    @Override
    public <K> PagedList<K> fetchPage(CriteriaBuilder<K> builder, Pageable pageable, String alias) {
        String orderColumn = pageable.getOrderByColumn();
        if (StringUtils.isNoneBlank(orderColumn)) builder.orderBy(alias + "." + orderColumn.trim(), pageable.getSort().isAscending());
        builder.setFirstResult(pageable.offset()).setMaxResults(pageable.getPageSize());
        return builder.page(pageable.offset(), pageable.getPageSize().intValue()).getResultList();
    }

    public Query nativeQuery(String sql) {
        return em.createNativeQuery(sql);
    }

    public <K> Query nativeQuery(String sql, Class<K> cls) {
        return em.createNativeQuery(sql, cls);
    }
}
