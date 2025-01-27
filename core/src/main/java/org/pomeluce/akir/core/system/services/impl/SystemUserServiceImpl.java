package org.pomeluce.akir.core.system.services.impl;

import jakarta.annotation.Resource;
import org.pomeluce.akir.common.core.page.Pageable;
import org.pomeluce.akir.core.system.domain.entity.User;
import org.pomeluce.akir.core.system.repository.SystemUserRepository;
import org.pomeluce.akir.core.system.services.SystemUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : lucas
 * @version 1.0
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
    public List<User> selectUserList(User user, Pageable pageable) {
        return repository.findUserList(user, pageable).orElse(List.of());
    }

    /**
     * 根据用户 id 查询用户信息
     *
     * @param id 用户 id
     * @return 用户信息
     */
    @Override
    public User selectUserById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
