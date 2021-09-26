package com.jmsoftware.maf.authcenter.role.entity;

import com.jmsoftware.maf.authcenter.role.entity.persistence.Role;
import com.jmsoftware.maf.springcloudstarter.poi.ExcelColumn;
import lombok.Data;
import lombok.val;

import javax.validation.constraints.NotBlank;

/**
 * Description: RoleExcelBean, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/22/2021 10:52 AM
 **/
@Data
public class RoleExcelBean {
    /**
     * Role name
     */
    @NotBlank
    @ExcelColumn(name = "Name")
    private String name;
    /**
     * Role description
     */
    @NotBlank
    @ExcelColumn(name = "Description")
    private String description;

    /**
     * Transform by role.
     *
     * @param role the role
     * @return the role excel import
     */
    public static RoleExcelBean transformBy(Role role) {
        val roleExcelImport = new RoleExcelBean();
        roleExcelImport.setName(role.getName());
        roleExcelImport.setDescription(role.getDescription());
        return roleExcelImport;
    }

    /**
     * Transform by role.
     *
     * @param roleExcelBean the role excel import
     * @return the role excel import
     */
    public static Role transformTo(RoleExcelBean roleExcelBean) {
        val role = new Role();
        role.setName(roleExcelBean.getName());
        role.setDescription(roleExcelBean.getDescription());
        return role;
    }
}
