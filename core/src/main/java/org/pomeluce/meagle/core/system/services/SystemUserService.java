package org.pomeluce.meagle.core.system.services;


import org.pomeluce.meagle.common.core.page.Pageable;
import org.pomeluce.meagle.core.system.domain.entity.User;

import java.util.List;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/12 20:18
 * @className : SystemUserService
 * @description : 用户对象业务接口
 */
public interface SystemUserService {
    /**
     * 查询用户列表
     *
     * @param user     查询条件
     * @param pageable 分页信息
     * @return 返回符合条件的用户信息列表
     */
    List<User> selectUserList(User user, Pageable pageable);

    /**
     * 根据用户 id 查询用户信息
     *
     * @param id 用户 id
     * @return 用户信息
     */
    User selectUserById(Long id);
}
