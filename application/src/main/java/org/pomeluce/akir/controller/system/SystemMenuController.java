package org.pomeluce.akir.controller.system;

import jakarta.annotation.Resource;
import org.pomeluce.akir.common.annotation.RestApiController;
import org.pomeluce.akir.common.core.controller.BaseController;
import org.pomeluce.akir.common.core.page.PaginationSupport;
import org.pomeluce.akir.server.system.domain.entity.Menu;
import org.pomeluce.akir.server.system.services.SystemMenuService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/20 21:21
 * @className : SystemMenuController
 * @description : 菜单请求控制器
 */
@RestApiController("/menu")
public class SystemMenuController extends BaseController {

    private @Resource SystemMenuService service;

    public @GetMapping("/list") List<Menu> find(Menu menu) {
        return service.find(menu, PaginationSupport.pageable());
    }
}
