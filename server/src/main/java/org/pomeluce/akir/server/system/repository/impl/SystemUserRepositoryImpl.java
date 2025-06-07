package org.pomeluce.akir.server.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import jakarta.persistence.EntityManager;
import org.pomeluce.akir.common.core.page.Pageable;
import org.pomeluce.akir.common.core.repository.BPWhereBuilder;
import org.pomeluce.akir.common.core.repository.BaseRepositoryImpl;
import org.pomeluce.akir.server.system.domain.entity.User;
import org.pomeluce.akir.server.system.repository.SystemUserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/12 00:00
 * @className : SystemUserRepositoryImpl
 * @description : User 数据持久层实现
 */
@Repository
@Transactional
public class SystemUserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements SystemUserRepository {

    public SystemUserRepositoryImpl(EntityManager entityManager, CriteriaBuilderFactory factory) {
        super(User.class, entityManager, factory);
    }

    /**
     * 根据条件查询用户, 分页
     *
     * @param user     查询条件
     * @param pageable 分页对象
     * @return 返回符合条件的用户信息集合
     */
    @Override
    public @Transactional(readOnly = true) Optional<PagedList<User>> find(User user, Pageable pageable) {
        CriteriaBuilder<User> cb = BPWhereBuilder.builder(factory.create(em, User.class, "user"), "user")
                .notEmptyEq(user.getId(), "id")
                .notEmptyLike(user.getAccount(), "account")
                .notEmptyLike(user.getEmail(), "email")
                .notEmptyEq(user.getStatus(), "status")
                .notEmptyLike(user.getCreateBy(), "createBy")
                .notEmptyLike(user.getUpdateBy(), "updateBy")
                .build();
        cb.orderByAsc("user.id");
        return Optional.ofNullable(this.fetchPage(cb, pageable, "user"));
    }

    /**
     * 根据账号查询用户
     *
     * @param account 账号
     * @return 返回用符合条件的用户信息
     */
    @Override
    public @Transactional(readOnly = true) Optional<User> findByAccount(String account) {
        return Optional.ofNullable(factory.create(em, User.class, "user").where("user.account").eq(account).getSingleResult());
    }
}
