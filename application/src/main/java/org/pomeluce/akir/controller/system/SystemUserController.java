package org.pomeluce.akir.controller.system;

import jakarta.annotation.Resource;
import org.pomeluce.akir.common.annotation.RestApiController;
import org.pomeluce.akir.common.core.controller.BaseController;
import org.pomeluce.akir.common.core.page.PaginationSupport;
import org.pomeluce.akir.common.utils.spring.SecurityUtils;
import org.pomeluce.akir.core.web.service.AkirTokenService;
import org.pomeluce.akir.server.system.domain.entity.User;
import org.pomeluce.akir.server.system.services.SystemUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/21 17:25
 * @className : SystemUserController
 * @description : 用户请求控制器
 */
@RestApiController("/user")
public class SystemUserController extends BaseController {

    private @Resource SystemUserService service;

    /* 查询当前用户 */
    public @GetMapping("/current") User current() {
        String account = (String) SecurityUtils.getAuthentication().getPrincipal();
        return service.findByAccount(account);
    }

    /* 查询用户列表 */
    public @GetMapping("/list") List<User> find(User user) {
        return service.find(user, PaginationSupport.pageable());
    }

    /* 根据用户 id 查询用户 */
    public @GetMapping("/{id}") User findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }
}
