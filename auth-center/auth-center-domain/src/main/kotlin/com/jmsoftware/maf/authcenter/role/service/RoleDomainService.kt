package com.jmsoftware.maf.authcenter.role.service

import com.baomidou.mybatisplus.extension.service.IService
import com.jmsoftware.maf.authcenter.role.RoleExcelBean
import com.jmsoftware.maf.authcenter.role.persistence.Role
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

/**
 * # RoleDomainService
 *
 * Domain Service of Role. (Role)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/10/22 5:23 PM
 */
@Validated
interface RoleDomainService : IService<Role> {
    companion object {
        const val ROLE_TEMPLATE_EXCEL = "role-stat.xlsx"
    }

    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    fun getRoleList(userId: @NotNull Long): GetRoleListByUserIdResponse

    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    fun getRoleListByUserId(userId: Long): List<GetRoleListByUserIdSingleResponse>

    /**
     * Check admin boolean.
     *
     * @param roleIdList the role id list
     * @return the boolean
     * @see <a href='https://baomidou.com/pages/10c804/#kotlin%E4%BD%BF%E7%94%A8wrapper'>Kotlin Wrapper for MyBatis Plus</a>
     */
    fun checkAdmin(roleIdList: @NotEmpty List<Long>): Boolean

    /**
     * Gets list for exporting.
     *
     * @return the list for exporting
     */
    fun getListForExporting(): List<RoleExcelBean>

    /**
     * Validate before add to bean list boolean.
     *
     * @param beanList the bean list
     * @param bean     the bean
     * @param index    the index
     * @throws IllegalArgumentException the illegal argument exception
     */
    fun validateBeforeAddToBeanList(beanList: List<RoleExcelBean>, bean: RoleExcelBean, index: Int)

    /**
     * Save.
     *
     * @param beanList the bean list
     */
    fun save(beanList: @NotEmpty List<RoleExcelBean>)
}
