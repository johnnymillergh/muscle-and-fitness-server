package com.jmsoftware.maf.authcenter.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <h1>UserPersistence</h1>
 * <p>
 * User Persistence object class
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 5/10/20 12:12 PM
 */
@Data
public class UserPersistence implements Serializable {
    private static final long serialVersionUID = -11418821727467072L;
    /**
     * Primary key of user
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
     * Full name
     */
    private String fullName;
    /**
     * Birthday
     */
    private Date birthday;
    /**
     * 26 gender options
     */
    private String gender;
    /**
     * UserPersistence avatar full path on SFTP server
     */
    private String avatar;
    /**
     * Status. 1 - enabled, 2 - disabled
     */
    private Integer status;
    /**
     * Created time
     */
    private Date createdTime;
    /**
     * Modified time
     */
    private Date modifiedTime;
}
