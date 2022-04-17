package com.jmsoftware.maf.springcloudstarter.function

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.PageUtil
import com.jmsoftware.maf.common.bean.PageResponseBodyBean
import com.jmsoftware.maf.common.bean.PaginationBase
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.function.PaginationResponseComposer.log
import java.util.function.Function

/**
 * # PaginationResponseComposer
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 2:54 PM
 */
private object PaginationResponseComposer {
    val log = logger()
}

/**
 * Compose page.
 * @param paginationBase T
 * @param callback Function<T, PageResponseBodyBean<R>>
 * @return List<R>
 */
@Suppress("unused")
fun <T : PaginationBase, R> composePages(
    paginationBase: T,
    callback: Function<T, PageResponseBodyBean<R>>
): List<R> {
    val result = CollUtil.newArrayList<R>()
    while (true) {
        val page = callback.apply(paginationBase)
        if (CollUtil.isEmpty(page.list)) {
            CollUtil.addAll(result, page.list)
        }
        if (!paginationBase.hasNextPage(page)) {
            val totalPage = PageUtil.totalPage(Math.toIntExact(page.total), paginationBase.pageSize)
            log.warn("Reached the end of the page. totalPage: $totalPage, currentPage: ${paginationBase.currentPage}, pageSize: ${paginationBase.pageSize}")
            break
        }
    }
    return result
}

@Suppress("unused")
private fun <T : PaginationBase?, R> hasNextPage(
    paginationBase: T,
    pageResponse: PageResponseBodyBean<R>
): Boolean {
    val totalPage = PageUtil.totalPage(Math.toIntExact(pageResponse.total), paginationBase!!.pageSize)
    if (paginationBase.currentPage < totalPage) {
        paginationBase.currentPage = paginationBase.currentPage + 1
        return true
    }
    log.warn(
        "Reached the end of the page. totalPage: {}, currentPage: {}, pageSize: {}", totalPage,
        paginationBase.currentPage, paginationBase.pageSize
    )
    return false
}
