package org.pomeluce.akir.server.system.repository;

import com.blazebit.persistence.PagedList;
import org.pomeluce.akir.common.core.page.Pageable;
import org.pomeluce.akir.common.core.repository.BaseRepository;
import org.pomeluce.akir.server.system.domain.entity.User;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/10 00:23
 * @className : SystemUserRepository
 * @description : User 数据持久层
 */
@NoRepositoryBean
public interface SystemUserRepository extends BaseRepository<User, Long> {
    /**
     * 根据条件查询用户, 分页
     *
     * @param user     查询条件
     * @param pageable 分页对象
     * @return 返回符合条件的用户信息集合
     */
    Optional<PagedList<User>> find(User user, Pageable pageable);

    /**
     * 根据账号查询用户
     *
     * @param account 账号
     * @return 返回用符合条件的用户信息
     */
    Optional<User> findByAccount(String account);
}
