package com.jmsoftware.maf.common.domain.authcenter.security;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import com.jmsoftware.maf.common.domain.authcenter.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <h1>UserPrincipal</h1>
 * <p>Custom user details.</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 20:52
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    @Serial
    private static final long serialVersionUID = -53353171692896501L;

    /**
     * Primary key
     */
    private Long id;
    /**
     * Username
     */
    private String username;
    /**
     * Email
     */
    private String email;
    /**
     * Phone
     */
    private String cellphone;
    /**
     * Password
     */
    @JsonIgnore
    private String password;
    /**
     * Nickname
     */
    private String fullName;
    /**
     * Birthday
     */
    private LocalDate birthday;
    /**
     * Gender
     */
    private String gender;
    /**
     * Status
     */
    private Byte status;
    /**
     * Create time
     */
    private LocalDateTime gmtCreated;
    /**
     * Modify time
     */
    private LocalDateTime gmtModified;
    /**
     * Roles that user has
     */
    private List<String> roles;
    /**
     * Authorities that user has
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Create by username user principal.
     *
     * @param username the username
     * @return the user principal
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/24/2020 3:12 PM
     */
    public static UserPrincipal createByUsername(String username) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUsername(username);
        return userPrincipal;
    }

    /**
     * Create user principal
     *
     * @param user           user
     * @param roleNameList   role name list
     * @param permissionList permission po list
     * @return user principal
     */
    public static UserPrincipal create(GetUserByLoginTokenResponse user, List<String> roleNameList,
                                       List<GetPermissionListByRoleIdListResponse.Permission> permissionList) {
        val permissions =
                Optional.ofNullable(permissionList).orElse(new LinkedList<>());
        val authorities = permissions.stream()
                .filter(permission -> StrUtil.isNotBlank(permission.getPermissionExpression()))
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionExpression()))
                .toList();

        return new UserPrincipal(user.getId(),
                                 user.getUsername(),
                                 user.getEmail(),
                                 user.getCellphone(),
                                 user.getPassword(),
                                 user.getFullName(),
                                 user.getBirthday(),
                                 user.getGender(),
                                 user.getStatus(),
                                 user.getCreatedTime(),
                                 user.getModifiedTime(),
                                 roleNameList,
                                 authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(this.status, UserStatus.ENABLED.getValue());
    }
}
