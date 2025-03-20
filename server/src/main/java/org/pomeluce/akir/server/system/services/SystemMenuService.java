package org.pomeluce.akir.server.system.services;

import org.pomeluce.akir.common.core.page.Pageable;
import org.pomeluce.akir.server.system.domain.entity.Menu;

import java.util.List;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/20 20:52
 * @className : SystemMenuService
 * @description : 菜单对象业务接口
 */
public interface SystemMenuService {
    /**
     * 查询菜单列表
     *
     * @param menu     查询条件
     * @param pageable 分页信息
     * @return 返回符合条件的菜单信息列表
     */
    List<Menu> find(Menu menu, Pageable pageable);
}
