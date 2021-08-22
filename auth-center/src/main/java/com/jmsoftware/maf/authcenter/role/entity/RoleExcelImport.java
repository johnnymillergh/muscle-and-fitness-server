package com.jmsoftware.maf.authcenter.role.entity;

import com.jmsoftware.maf.authcenter.role.entity.persistence.Role;
import com.jmsoftware.maf.springcloudstarter.annotation.ExcelColumn;
import lombok.Data;
import lombok.val;

import javax.validation.constraints.NotBlank;

/**
 * Description: RoleExcelImport, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/22/2021 10:52 AM
 **/
@Data
public class RoleExcelImport {
    /**
     * Role name
     */
    @NotBlank
    @ExcelColumn(description = "Name")
    private String name;
    /**
     * Role description
     */
    @NotBlank
    @ExcelColumn(description = "Description")
    private String description;

    /**
     * Transform by role.
     *
     * @param role the role
     * @return the role excel import
     */
    public static RoleExcelImport transformBy(Role role) {
        val roleExcelImport = new RoleExcelImport();
        roleExcelImport.setName(role.getName());
        roleExcelImport.setDescription(role.getDescription());
        return roleExcelImport;
    }

    /**
     * Transform by role.
     *
     * @param roleExcelImport the role excel import
     * @return the role excel import
     */
    public static Role transformTo(RoleExcelImport roleExcelImport) {
        val role = new Role();
        role.setName(roleExcelImport.getName());
        role.setDescription(roleExcelImport.getDescription());
        return role;
    }
}
