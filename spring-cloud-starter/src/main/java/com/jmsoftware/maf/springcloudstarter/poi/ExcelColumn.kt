package com.jmsoftware.maf.springcloudstarter.poi

import org.apache.poi.ss.usermodel.CellType

/**
 * # ExcelColumn
 *
 * Description: ExcelColumn, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 3:57 PM
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelColumn(
    /**
     * Column name.
     *
     * @return the string
     */
    val name: String = "Default Column Name",
    /**
     * Cell type.
     *
     * @return the cell type
     */
    val cellType: CellType = CellType.STRING
)
