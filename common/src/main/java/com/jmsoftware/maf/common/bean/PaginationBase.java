package com.jmsoftware.maf.common.bean;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * <h1>PaginationBase</h1>
 * <p>
 * Pagination Base.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/17/20 11:18 PM
 **/
@Data
@SuppressWarnings("unused")
public class PaginationBase {
    /**
     * The current page. Default: 1
     */
    @JsonIgnore
    @NotNull(message = "The current page is required!")
    @Min(value = 1, message = "The current page is not less then 1!")
    private Integer currentPage = 1;
    /**
     * The page size. Default: 10
     */
    @JsonIgnore
    @NotNull(message = "The page size is required！")
    @Range(min = 10, max = 100, message = "The rage of page size: 10 <= page size <= 100!")
    private Integer pageSize = 10;
    /**
     * The order-by. (for table's field)
     */
    @JsonIgnore
    private String orderBy;
    /**
     * The order rule. Default: DESC
     */
    @JsonIgnore
    private String orderRule = "DESC";
    /**
     * The order-by statement needs to be joined.
     */
    @JsonIgnore
    private String orderByStatement;

    @JsonIgnore
    public String getOrderByStatement() {
        if (!StrUtil.isBlank(orderBy)) {
            return String.format("%s %s %s", "ORDER BY", orderBy, orderRule);
        }
        return orderByStatement;
    }

    /**
     * Has next page boolean.
     *
     * @param pageResponseBodyBean the page response body bean
     * @return the boolean
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 4:37 PM
     */
    public boolean hasNextPage(@NotNull PageResponseBodyBean<?> pageResponseBodyBean) {
        if (NumberUtil.compare(pageResponseBodyBean.getTotal(), (long) currentPage * pageSize) > 0) {
            currentPage += 1;
            return true;
        }
        return false;
    }
}
