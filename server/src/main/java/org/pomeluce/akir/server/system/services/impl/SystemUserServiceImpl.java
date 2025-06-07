package org.pomeluce.akir.server.system.services.impl;

import com.blazebit.persistence.PagedList;
import jakarta.annotation.Resource;
import org.pomeluce.akir.common.core.page.PageInfo;
import org.pomeluce.akir.common.core.page.Pageable;
import org.pomeluce.akir.common.core.page.PaginationSupport;
import org.pomeluce.akir.server.system.domain.entity.User;
import org.pomeluce.akir.server.system.repository.SystemUserRepository;
import org.pomeluce.akir.server.system.services.SystemUserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/12 20:20
 * @className : SystemUserServiceImpl
 * @description : 用户对象业务接口实现
 */
@Service
public class SystemUserServiceImpl implements SystemUserService {

    private @Resource SystemUserRepository repository;

    /**
     * 查询用户列表
     *
     * @param user     查询条件
     * @param pageable 分页信息
     * @return 返回符合条件的用户信息列表
     */
    @Override
    public PageInfo<User> find(User user, Pageable pageable) {
        Optional<PagedList<User>> users = repository.find(user, pageable);
        return users.isPresent() ? PageInfo.builder(users.get()).pageSize(pageable.getPageSize()).build() : PaginationSupport.emptyPageInfo();
    }

    /**
     * 根据账号查询用户信息
     *
     * @param account 用户名
     * @return 用户信息
     */
    public User findByAccount(String account) {
        return repository.findByAccount(account).orElse(null);
    }

    /**
     * 根据用户 id 查询用户信息
     *
     * @param id 用户 id
     * @return 用户信息
     */
    @Override
    public User findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
