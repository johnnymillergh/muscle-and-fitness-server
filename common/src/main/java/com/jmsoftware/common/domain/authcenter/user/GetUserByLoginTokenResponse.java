package com.jmsoftware.common.domain.authcenter.user;

import lombok.Data;

import java.util.Date;

/**
 * <h1>GetUserByLoginTokenResponse</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/10/20 12:45 PM
 **/
@Data
public class GetUserByLoginTokenResponse {
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
     * Cellphone number
     */
    private String cellphone;
    /**
     * Password
     */
    private String password;
    /**
     * Nickname
     */
    private String fullName;
    /**
     * Birthday (yyyy-MM-dd)
     */
    private Date birthday;
    /**
     * 58 gender options
     */
    private String gender;
    /**
     * User avatar full path on SFTP server
     */
    private String avatar;
    /**
     * Status. 1 - enabled, 2 - disabled
     */
    private Integer status;
    /**
     * Create time
     */
    private Date createdTime;
    /**
     * Modify time
     */
    private Date modifiedTime;
}
