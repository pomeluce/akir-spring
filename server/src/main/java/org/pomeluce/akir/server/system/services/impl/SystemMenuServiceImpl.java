package org.pomeluce.akir.server.system.services.impl;

import jakarta.annotation.Resource;
import org.pomeluce.akir.common.core.page.Pageable;
import org.pomeluce.akir.server.system.domain.entity.Menu;
import org.pomeluce.akir.server.system.repository.SystemMenuRepository;
import org.pomeluce.akir.server.system.services.SystemMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/20 20:55
 * @className : SystemMenuServiceImpl
 * @description : 菜单对象业务接口实现
 */
@Service
public class SystemMenuServiceImpl implements SystemMenuService {
    private @Resource SystemMenuRepository repository;

    /**
     * 查询菜单列表
     *
     * @param menu     查询条件
     * @param pageable 分页信息
     * @return 返回符合条件的菜单信息列表
     */
    @Override
    public List<Menu> find(Menu menu, Pageable pageable) {
        return repository.find(menu, pageable).orElse(List.of());
    }
}
