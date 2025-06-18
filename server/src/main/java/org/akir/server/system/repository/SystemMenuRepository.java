package org.akir.server.system.repository;

import org.akir.common.core.repository.BaseRepository;
import org.akir.server.system.domain.entity.Menu;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/3/20 20:56
 * @className : SystemMenuRepository
 * @description : Menu 数据持久层
 */
@NoRepositoryBean
public interface SystemMenuRepository extends BaseRepository<Menu, Long> {

    /**
     * 根据条件查询菜单
     *
     * @param menu 查询条件
     * @return 返回符合条件的菜单信息集合
     */
    Optional<List<Menu>> find(Menu menu);
}
