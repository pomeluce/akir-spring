package org.akir.common.core.page;

import lombok.*;
import org.akir.common.utils.StringUtils;
import org.springframework.data.domain.Sort;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/11 21:39
 * @className : Pageable
 * @description : 分页对象
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pageable implements Serializable {
    private static final @Serial long serialVersionUID = 1L;
    /* 当前页 */
    private Integer pageNumber = 1;
    /* 页容量 */
    private Integer pageSize = 10;
    /* 排序方式 */
    private Sort.Direction sort = Sort.Direction.ASC;
    /* 排序列 */
    private String orderByColumn;

    public int offset() {
        return (this.pageNumber - 1) * pageSize;
    }

    public String getOrderBy() {
        return StringUtils.isBlank(orderByColumn) ? "" : StringUtils.toSnakeCase(orderByColumn) + " " + sort.toString().toLowerCase();
    }

    public Optional<Sort> getJpaOrderBy() {
        if (StringUtils.isEmpty(this.orderByColumn)) return Optional.of(Sort.unsorted());
        Sort sort = Sort.by(this.orderByColumn);
        if (this.sort.isAscending()) sort.ascending();
        else sort.descending();
        return Optional.of(sort);
    }
}
