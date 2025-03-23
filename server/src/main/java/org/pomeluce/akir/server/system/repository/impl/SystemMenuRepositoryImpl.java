package org.pomeluce.akir.server.system.repository.impl;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.pomeluce.akir.common.core.repository.BaseRepositoryImpl;
import org.pomeluce.akir.common.core.repository.SelectBooleanBuilder;
import org.pomeluce.akir.server.system.domain.entity.Menu;
import org.pomeluce.akir.server.system.domain.entity.QMenu;
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
    final QMenu menu = QMenu.menu;

    public SystemMenuRepositoryImpl(EntityManager entityManager, BlazeJPAQueryFactory factory) {
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
        BlazeJPAQuery<Menu> query = factory.selectFrom(this.menu).where(SelectBooleanBuilder.builder()
                .notEmptyEq(menu.getCode(), this.menu.code)
                .notEmptyLike(menu.getLabel(), this.menu.label)
                .notEmptyEq(menu.getShow(), this.menu.show)
                .notEmptyEq(menu.getDisabled(), this.menu.disabled)
                .notEmptyLike(menu.getTarget(), this.menu.target)
                .build()
        );
        return Optional.of(query.fetch());
    }
}
