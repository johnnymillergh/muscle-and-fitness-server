package com.jmsoftware.maf.common.domain.authcenter.security

import cn.hutool.core.util.StrUtil
import com.fasterxml.jackson.annotation.JsonIgnore
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse
import com.jmsoftware.maf.common.domain.authcenter.user.UserStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serial
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * # UserPrincipal
 *
 * Custom user details.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 7:26 PM
 */
class UserPrincipal : UserDetails {
    /**
     * Primary key
     */
    var id: Long? = null

    /**
     * Username
     */
    var usernameProperty: String? = null

    /**
     * Email
     */
    var email: String? = null

    /**
     * Phone
     */
    var cellphone: String? = null

    /**
     * Password
     */
    @JsonIgnore
    var passwordProperty: String? = null

    /**
     * Nickname
     */
    var fullName: String? = null

    /**
     * Birthday
     */
    var birthday: LocalDate? = null

    /**
     * Gender
     */
    var gender: String? = null

    /**
     * Status
     */
    var status: Byte? = null

    /**
     * Create time
     */
    var gmtCreated: LocalDateTime? = null

    /**
     * Modify time
     */
    var gmtModified: LocalDateTime? = null

    /**
     * Roles that user has
     */
    var roles: List<String>? = null

    /**
     * Authorities that user has
     */
    var authoritiesProperty: Collection<GrantedAuthority>? = null

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authoritiesProperty!!
    }

    override fun getPassword(): String {
        return passwordProperty!!
    }

    override fun getUsername(): String {
        return usernameProperty!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return status == UserStatus.ENABLED.value
    }

    companion object {
        @Serial
        private const val serialVersionUID = -53353171692896501L

        /**
         * Create by username user principal.
         *
         * @param username the username
         * @return the user principal
         * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/24/2020 3:12 PM
         */
        fun createByUsername(username: String): UserPrincipal {
            val userPrincipal = UserPrincipal()
            userPrincipal.usernameProperty = username
            return userPrincipal
        }

        /**
         * Create user principal
         *
         * @param user           user
         * @param roleNameList   role name list
         * @param permissionList permission po list
         * @return user principal
         */
        fun create(
            user: GetUserByLoginTokenResponse,
            roleNameList: List<String> = listOf(),
            permissionList: List<GetPermissionListByRoleIdListResponse.Permission> = listOf()
        ): UserPrincipal {
            val authorities = permissionList.stream()
                .filter { permission: GetPermissionListByRoleIdListResponse.Permission -> StrUtil.isNotBlank(permission.permissionExpression) }
                .map { permission: GetPermissionListByRoleIdListResponse.Permission -> SimpleGrantedAuthority(permission.permissionExpression) }
                .toList()
            return UserPrincipal().apply {
                this.id = user.id
                this.usernameProperty = user.username
                this.email = user.email
                this.cellphone = user.cellphone
                this.passwordProperty = user.password
                this.fullName = user.fullName
                this.birthday = user.birthday
                this.gender = user.gender
                this.status = user.status
                this.gmtCreated = user.createdTime
                this.gmtModified = user.modifiedTime
                this.roles = roleNameList
                this.authoritiesProperty = authorities
            }
        }
    }
}
