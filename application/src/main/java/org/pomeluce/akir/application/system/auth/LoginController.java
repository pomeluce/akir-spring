package org.pomeluce.akir.application.system.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.pomeluce.akir.common.annotation.RestApiController;
import org.pomeluce.akir.common.core.controller.BaseController;
import org.pomeluce.akir.common.core.domain.HttpEntity;
import org.pomeluce.akir.core.system.domain.model.LoginBody;
import org.pomeluce.akir.core.web.service.AkirLoginService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2023/9/27 23:38
 * @className : LoginController
 * @description : 登录控制器
 */
@RestApiController("/auth")
@Tag(name = "登录控制器")
public class LoginController extends BaseController {
    private @Resource AkirLoginService service;

    @Operation(summary = "用户登录")
    public @PostMapping("/login") HttpEntity<String, Object> login(@RequestBody LoginBody loginBody) {
        String token = service.login(loginBody.account(), loginBody.password());
        HttpEntity<String, Object> result = HttpEntity.instance(HttpStatus.OK.value());
        return result.put("用户登录成功", token);
    }
}
