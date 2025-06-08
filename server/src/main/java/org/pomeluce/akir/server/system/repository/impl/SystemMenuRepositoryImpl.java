package org.pomeluce.akir.server.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import jakarta.persistence.EntityManager;
import org.pomeluce.akir.common.core.repository.BPWhereBuilder;
import org.pomeluce.akir.common.core.repository.BaseRepositoryImpl;
import org.pomeluce.akir.server.system.domain.entity.Menu;
import org.pomeluce.akir.server.system.repository.SystemMenuRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/20 20:58
 * @className : SystemMenuRepositoryImpl
 * @description : Menu 数据持久层实现
 */
@Repository
@Transactional
public class SystemMenuRepositoryImpl extends BaseRepositoryImpl<Menu, Long> implements SystemMenuRepository {
    public SystemMenuRepositoryImpl(EntityManager entityManager, CriteriaBuilderFactory factory) {
        super(Menu.class, entityManager, factory);
    }

    /**
     * 根据条件查询菜单
     *
     * @param menu 查询条件
     * @return 返回符合条件的菜单信息集合
     */
    @Override
    public @Transactional(readOnly = true) Optional<List<Menu>> find(Menu menu) {
        CriteriaBuilder<Menu> cb = BPWhereBuilder.builder(factory.create(em, Menu.class, "menu"))
                .notEmptyEq(menu.getCode(), "code")
                .notEmptyLike(menu.getLabel(), "label")
                .notEmptyEq(menu.getShow(), "show")
                .notEmptyEq(menu.getDisabled(), "disabled")
                .notEmptyLike(menu.getTarget(), "target")
                .build();
        return Optional.ofNullable(cb.getResultList());
    }
}
