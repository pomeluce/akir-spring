package org.akir.common.core.page;

import com.blazebit.persistence.PagedList;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/6/7 11:53
 * @className : PageInfo
 * @description : 分页结果响应对象
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PageInfo<T> implements Serializable {
    private static final @Serial long serialVersionUID = 1L;

    /* 当前页 */
    private Integer pageNo;
    /* 页容量 */
    private Integer pageSize;
    /* 总页数 */
    private Integer totalPages;
    /* 总条数 */
    private Long totalRecords;
    /* 查询数据 */
    private List<T> data;

    public static <T> PageInfoBuilder<T> builder(PagedList<T> pagedList) {
        PageInfoBuilder<T> builder = new PageInfoBuilder<T>();
        builder.pageNo(pagedList.getPage());
        builder.totalPages(pagedList.getTotalPages());
        builder.totalRecords(pagedList.getTotalSize());
        builder.data(pagedList);
        return builder;
    }
}
