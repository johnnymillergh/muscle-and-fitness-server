package com.jmsoftware.maf.authcenter.role

import com.jmsoftware.maf.authcenter.role.persistence.Role
import com.jmsoftware.maf.springcloudstarter.poi.ExcelColumn
import javax.validation.constraints.NotBlank

/**
 * # RoleExcelBean
 *
 * Description: RoleExcelBean, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:40 PM
 */
class RoleExcelBean {
    /**
     * Role name
     */
    @ExcelColumn(name = "Name")
    private lateinit var name: @NotBlank String

    /**
     * Role description
     */
    @ExcelColumn(name = "Description")
    private lateinit var description: @NotBlank String

    companion object {
        /**
         * Transform by role.
         *
         * @param role the role
         * @return the role excel import
         */
        fun transformBy(role: Role): RoleExcelBean {
            val roleExcelImport = RoleExcelBean()
            roleExcelImport.name = role.name
            roleExcelImport.description = role.description
            return roleExcelImport
        }

        /**
         * Transform by role.
         *
         * @param roleExcelBean the role excel import
         * @return the role excel import
         */
        fun transformTo(roleExcelBean: RoleExcelBean): Role {
            val role = Role()
            role.name = roleExcelBean.name
            role.description = roleExcelBean.description
            return role
        }
    }
}
