package com.jmsoftware.maf.springcloudstarter.annotation;

import org.apache.poi.ss.usermodel.CellType;

import java.lang.annotation.*;

/**
 * Description: ExcelColumn, change description here.
 *
 * @author 钟俊 （zhongjun）, email: zhongjun@toguide.cn, date: 2/19/2021 9:15 AM
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    /**
     * Column name.
     *
     * @return the string
     */
    String name() default "Default Column Name";

    /**
     * Cell type cell type.
     *
     * @return the cell type
     */
    CellType cellType() default CellType.STRING;
}
