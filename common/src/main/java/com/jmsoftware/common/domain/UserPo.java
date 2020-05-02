package com.jmsoftware.common.domain;

import lombok.Data;

import java.util.Date;

/**
 * <h1>UserPo</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 3/13/20 9:53 AM
 **/
@Data
public class UserPo {
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
    private Date gmtCreated;
    /**
     * Modify time
     */
    private Date gmtModified;
}
