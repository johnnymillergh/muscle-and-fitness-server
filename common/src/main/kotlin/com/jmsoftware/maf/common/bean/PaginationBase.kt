package com.jmsoftware.maf.common.bean

import cn.hutool.core.util.NumberUtil
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

/**
 * PaginationBase
 *
 * Pagination Base.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:13 AM
 */
open class PaginationBase {
    /**
     * The current page. Default: 1
     */
    @JsonIgnore
    @NotNull(message = "The current page is required!")
    @Min(value = 1L, message = "The current page is not less then 1!")
    var currentPage: Int = 1

    /**
     * The page size. Default: 10
     */
    @JsonIgnore
    @NotNull(message = "The page size is required!")
    @Range(min = 10L, max = 100L, message = "The rage of page size: 10 <= page size <= 100!")
    var pageSize: Int = 10

    /**
     * The order-by. (for table's field)
     */
    @JsonIgnore
    var orderBy: String? = null

    /**
     * The order rule. Default: DESC
     */
    @JsonIgnore
    @Pattern(regexp = "^(ASC|DESC)$")
    var orderRule: String = "DESC"

    /**
     * The order-by statement needs to be joined.
     */
    @JsonIgnore
    var orderByStatement: String? = null

    /**
     * The order-by statement needs to be joined.
     */
    @JsonIgnore
    fun orderByStatement(): String {
        orderByStatement?.isBlank()
        if (orderBy?.isNotBlank() == true) {
            return "ORDER BY `$orderBy` $orderRule"
        }
        return orderByStatement!!
    }

    /**
     * Has next page boolean.
     *
     * @param pageResponseBodyBean the page response body bean
     * @return the boolean
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 4:37 PM
     */
    fun hasNextPage(pageResponseBodyBean: @NotNull PageResponseBodyBean<*>?): Boolean {
        if (NumberUtil.compare(pageResponseBodyBean!!.total, currentPage.toLong() * pageSize) > 0) {
            currentPage += 1
            return true
        }
        return false
    }
}
