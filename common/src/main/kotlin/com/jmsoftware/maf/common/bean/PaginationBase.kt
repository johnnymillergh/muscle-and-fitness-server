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
@Suppress("MemberVisibilityCanBePrivate")
open class PaginationBase {
    /**
     * The current page. Default: 1
     */
    @NotNull(message = "The current page is required!")
    @Min(value = 1L, message = "The current page is not less then 1!")
    var currentPage: Int = 0

    /**
     * The page size. Default: 10
     */
    @NotNull(message = "The page size is required!")
    @Range(min = 10L, max = 100L, message = "The rage of page size: 10 <= page size <= 100!")
    val pageSize: Int = 10

    /**
     * The order-by. (for table's field)
     */
    val orderBy: String? = null

    /**
     * The order rule. Default: DESC
     */
    @Pattern(regexp = "^(ASC|DESC)$")
    val orderRule: String = "DESC"

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
