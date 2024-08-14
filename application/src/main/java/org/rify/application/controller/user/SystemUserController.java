package org.rify.application.controller.user;

import jakarta.annotation.Resource;
import org.rify.common.annotation.RestApiController;
import org.rify.common.core.controller.BaseController;
import org.rify.common.core.domain.HttpEntity;
import org.rify.common.core.page.PaginationSupport;
import org.rify.common.utils.spring.SecurityUtils;
import org.rify.server.system.domain.entity.User;
import org.rify.server.system.domain.model.LoginUser;
import org.rify.server.system.services.SystemUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/21下午5:25
 * @className : SystemUserController
 * @description : 用户请求控制器
 */
@RestApiController("/user")
public class SystemUserController extends BaseController {

    private @Resource SystemUserService service;

    /* 查询当前用户 */
    public @GetMapping("/current") HttpEntity<User, Object> currentUser() {
        LoginUser user = (LoginUser) SecurityUtils.getAuthentication().getPrincipal();
        HttpEntity<User, Object> result = HttpEntity.instance(HttpStatus.OK.value());
        return result.put("当前登录用户", user.getUser()).put(new HashMap<>() {{
            put("role", "");
        }});
    }

    /* 查询用户列表 */
    public @GetMapping("/list") HttpEntity<List<User>, Object> list(User user) {
        List<User> users = service.selectUserList(user, PaginationSupport.pageable());
        return success("用户列表查询成功", users);
    }

    /* 根据用户 id 查询用户 */
    public @GetMapping("/{id}") HttpEntity<User, Object> selectByAccount(@PathVariable(value = "id") Long id) {
        User user = service.selectUserById(id);
        return success("用户查询成功", user);
    }
}
