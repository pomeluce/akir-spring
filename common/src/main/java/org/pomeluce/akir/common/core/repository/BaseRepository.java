package org.pomeluce.akir.common.core.repository;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import org.pomeluce.akir.common.core.page.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/11 21:25
 * @className : BaseRepository
 * @description : 通用持久层接口
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    void entityClear();

    void entityDetach(T entity);

    <K> Optional<List<K>> fetchPage(BlazeJPAQuery<K> query, Pageable pageable);
}
