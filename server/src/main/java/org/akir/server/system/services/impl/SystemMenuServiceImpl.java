package org.akir.server.system.services.impl;

import lombok.RequiredArgsConstructor;
import org.akir.server.system.domain.entity.Menu;
import org.akir.server.system.repository.SystemMenuRepository;
import org.akir.server.system.services.SystemMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/20 20:55
 * @className : SystemMenuServiceImpl
 * @description : 菜单对象业务接口实现
 */
@RequiredArgsConstructor
@Service
public class SystemMenuServiceImpl implements SystemMenuService {
    private final SystemMenuRepository repository;

    /**
     * 查询菜单列表
     *
     * @param menu 查询条件
     * @return 返回符合条件的菜单信息列表
     */
    @Override
    public List<Menu> find(Menu menu) {
        return repository.find(menu).orElse(List.of());
    }
}
