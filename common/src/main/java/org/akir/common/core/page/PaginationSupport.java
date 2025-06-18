package org.akir.common.core.page;

import com.blazebit.persistence.PagedList;
import org.akir.common.utils.spring.ServletClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/12 23:51
 * @className : PaginationSupport
 * @description : 数据分页支持器
 */
public class PaginationSupport {
    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_SIZE = "pageSize";
    private static final String ORDER_BY_COLUMN = "orderByColumn";
    private static final String SORT = "sort";

    public static Pageable pageable() {
        return Pageable.builder()
                .pageNumber(ServletClient.getIntegerParameter(PAGE_NUMBER, 1))
                .pageSize(ServletClient.getIntegerParameter(PAGE_SIZE, 10))
                .orderByColumn(ServletClient.getParameter(ORDER_BY_COLUMN))
                .sort(direction(ServletClient.getParameter(SORT, "desc").toUpperCase()))
                .build();
    }

    public static PageRequest pageRequest() {
        Pageable pageable = pageable();
        return pageable.getJpaOrderBy().map(order -> PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), order)).orElse(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }

    static Sort.Direction direction(String value) {
        try {
            return Sort.Direction.valueOf(value);
        } catch (IllegalArgumentException e) {
            return Sort.Direction.ASC;
        }
    }

    public static @SuppressWarnings("unchecked") <T> PageInfo<T> emptyPageInfo() {
        return PageInfo.builder((PagedList<T>) PagedList.EMPTY).pageSize(ServletClient.getIntegerParameter(PAGE_SIZE, 10)).build();
    }

}
