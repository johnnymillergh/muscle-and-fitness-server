package com.jmsoftware.maf.common.bean

import cn.hutool.core.util.NumberUtil
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Range

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
    @NotNull
    @Min(value = 1L)
    var currentPage: Int = 1

    /**
     * The page size. Default: 10
     */
    @NotNull
    @Range(min = 10L, max = 100L)
    var pageSize: Int = 10

    /**
     * The order-by. (for table's field)
     */
    var orderBy: String? = null

    /**
     * The order rule. Default: DESC
     */
    @Pattern(regexp = "^(ASC|DESC)$")
    var orderRule: String = "DESC"

    /**
     * The order-by statement needs to be joined.
     */
    fun orderByStatement(): String {
        if (orderBy?.isNotBlank() == true) {
            return "ORDER BY `$orderBy` $orderRule"
        }
        return ""
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
