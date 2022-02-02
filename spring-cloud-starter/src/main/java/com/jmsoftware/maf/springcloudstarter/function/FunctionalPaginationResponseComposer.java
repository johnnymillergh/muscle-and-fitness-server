package com.jmsoftware.maf.springcloudstarter.function;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PageUtil;
import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.common.bean.PaginationBase;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;
import java.util.function.Function;

import static java.lang.Math.toIntExact;

/**
 * <h1>FunctionalPaginationResponseComposer</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 2/1/22 1:13 AM
 **/
@Slf4j
@SuppressWarnings("unused")
public class FunctionalPaginationResponseComposer {
    private FunctionalPaginationResponseComposer() {
    }

    public static <T extends PaginationBase, R> List<R> composePages(
            @NonNull T paginationBase,
            @NonNull Function<T, PageResponseBodyBean<R>> callback
    ) {
        val result = CollUtil.<R>newArrayList();
        while (true) {
            val page = callback.apply(paginationBase);
            if (CollUtil.isEmpty(page.getList())) {
                CollUtil.addAll(result, page.getList());
            }
            if (!paginationBase.hasNextPage(page)) {
                val totalPage = PageUtil.totalPage(toIntExact(page.getTotal()), paginationBase.getPageSize());
                log.warn("Reached the end of the page. totalPage: {}, currentPage: {}, pageSize: {}", totalPage,
                         paginationBase.getCurrentPage(), paginationBase.getPageSize());
                break;
            }
        }
        return result;
    }

    private static <T extends PaginationBase, R> boolean hasNextPage(
            @NonNull T paginationBase,
            @NonNull PageResponseBodyBean<R> pageResponse
    ) {
        val totalPage = PageUtil.totalPage(toIntExact(pageResponse.getTotal()), paginationBase.getPageSize());
        if (paginationBase.getCurrentPage() < totalPage) {
            paginationBase.setCurrentPage(paginationBase.getCurrentPage() + 1);
            return true;
        }
        log.warn("Reached the end of the page. totalPage: {}, currentPage: {}, pageSize: {}", totalPage,
                 paginationBase.getCurrentPage(), paginationBase.getPageSize());
        return false;
    }
}
