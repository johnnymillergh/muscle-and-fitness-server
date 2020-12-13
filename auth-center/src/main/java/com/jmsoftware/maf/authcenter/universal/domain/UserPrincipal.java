package com.jmsoftware.maf.authcenter.universal.domain;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jmsoftware.maf.common.domain.authcenter.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Integer status;
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
     * @param user           user po
     * @param roleList       role po list
     * @param permissionList permission po list
     * @return user principal
     */
    public static UserPrincipal create(UserPO user, List<RolePO> roleList, List<PermissionPO> permissionList) {
        List<String> roleNames = roleList.stream().map(RolePO::getName).collect(Collectors.toList());

        List<GrantedAuthority> authorities =
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
                                 roleNames,
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
