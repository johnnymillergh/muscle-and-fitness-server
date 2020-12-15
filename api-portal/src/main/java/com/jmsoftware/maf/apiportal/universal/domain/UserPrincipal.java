package com.jmsoftware.maf.apiportal.universal.domain;

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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <h1>UserPrincipal</h1>
 * <p>Custom user details.</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 20:52
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
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
    private Date birthday;
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
    private Date gmtCreated;
    /**
     * Modify time
     */
    private Date gmtModified;
    /**
     * Roles that user has
     */
    private List<String> roles;
    /**
     * Authorities that user has
     */
    private Collection<? extends GrantedAuthority> authorities;

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
        val authorities =
                permissionList.stream()
                        .filter(permission -> StrUtil.isNotBlank(permission.getPermissionExpression()))
                        .map(permission -> new SimpleGrantedAuthority(permission.getPermissionExpression()))
                        .collect(Collectors.toList());

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
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return Objects.equals(this.status, UserStatus.ENABLED.getStatus());
    }
}
