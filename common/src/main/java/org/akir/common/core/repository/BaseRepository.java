package org.akir.common.core.repository;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.PagedList;
import org.akir.common.core.page.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/11 21:25
 * @className : BaseRepository
 * @description : 通用持久层接口
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    void entityClear();

    void entityDetach(T entity);

    <K> PagedList<K> fetchPage(CriteriaBuilder<K> builder, Pageable pageable);
}
