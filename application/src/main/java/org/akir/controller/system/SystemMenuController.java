package org.akir.controller.system;

import lombok.RequiredArgsConstructor;
import org.akir.common.annotation.RestAPIController;
import org.akir.common.core.controller.BaseController;
import org.akir.server.system.domain.entity.Menu;
import org.akir.server.system.services.SystemMenuService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/20 21:21
 * @className : SystemMenuController
 * @description : 菜单请求控制器
 */
@RequiredArgsConstructor
@RestAPIController("/menu")
public class SystemMenuController extends BaseController {

    private final SystemMenuService service;

    public @GetMapping("/list") List<Menu> find(Menu menu) {
        return service.find(menu);
    }
}
