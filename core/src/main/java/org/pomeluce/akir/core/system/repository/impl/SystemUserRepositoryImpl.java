package org.pomeluce.akir.core.system.repository.impl;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.pomeluce.akir.common.core.page.Pageable;
import org.pomeluce.akir.common.core.repository.BaseRepositoryImpl;
import org.pomeluce.akir.common.core.repository.SelectBooleanBuilder;
import org.pomeluce.akir.core.system.domain.entity.QUser;
import org.pomeluce.akir.core.system.domain.entity.User;
import org.pomeluce.akir.core.system.repository.SystemUserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/12 00:00
 * @className : SystemUserRepositoryImpl
 * @description : User 数据持久层实现
 */
@Repository
@Transactional
public class SystemUserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements SystemUserRepository {

    final QUser user = QUser.user;

    public SystemUserRepositoryImpl(EntityManager entityManager, BlazeJPAQueryFactory factory) {
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
    public @Transactional(readOnly = true) Optional<List<User>> findUserList(User user, Pageable pageable) {
        BlazeJPAQuery<User> query = factory.selectFrom(this.user).where(SelectBooleanBuilder.builder()
                .notEmptyEq(user.getId(), this.user.id)
                .notEmptyLike(user.getAccount(), this.user.account)
                .notEmptyLike(user.getEmail(), this.user.email)
                .notEmptyEq(user.getStatus(), this.user.status)
                .notEmptyEq(user.getRole(), this.user.role)
                .notEmptyLike(user.getCreateBy(), this.user.createBy)
                .notEmptyLike(user.getUpdateBy(), this.user.updateBy)
                .build()
        );
        return this.fetchPage(query, pageable);
    }

    /**
     * 根据账号查询用户
     *
     * @param account 账号
     * @return 返回用符合条件的用户信息
     */
    @Override
    public @Transactional(readOnly = true) Optional<User> findUserByAccount(String account) {
        return Optional.ofNullable(factory.selectFrom(this.user).where(this.user.account.eq(account)).fetchOne());
    }
}
