package com.jmsoftware.maf.authcenter.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <h1>UserPersistence</h1>
 * <p>
 * User Persistence object class
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 5/10/20 12:12 PM
 */
@Data
@TableName(value = "user")
public class UserPersistence implements Serializable {
    private static final long serialVersionUID = -11418821727467072L;

    /**
     * Primary key of user
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Username
     */
    @TableField(value = "username")
    private String username;

    /**
     * Email
     */
    @TableField(value = "email")
    private String email;

    /**
     * Cellphone number
     */
    @TableField(value = "cellphone")
    private String cellphone;

    /**
     * Password
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * Full name
     */
    @TableField(value = "full_name")
    private String fullName;

    /**
     * Birthday
     */
    @TableField(value = "birthday")
    private Date birthday;

    /**
     * 26 gender options
     */
    @TableField(value = "gender")
    private Object gender;

    /**
     * User avatar full path on SFTP server
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * Status. 1 - enabled, 2 - disabled
     */
    @TableField(value = "`status`")
    private Byte status;

    /**
     * Created time
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private Date createdTime;

    /**
     * Modified time
     */
    @TableField(value = "modified_time", fill = FieldFill.INSERT_UPDATE)
    private Date modifiedTime;

    public static final String COL_ID = "id";

    public static final String COL_USERNAME = "username";

    public static final String COL_EMAIL = "email";

    public static final String COL_CELLPHONE = "cellphone";

    public static final String COL_PASSWORD = "password";

    public static final String COL_FULL_NAME = "full_name";

    public static final String COL_BIRTHDAY = "birthday";

    public static final String COL_GENDER = "gender";

    public static final String COL_AVATAR = "avatar";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATED_TIME = "created_time";

    public static final String COL_MODIFIED_TIME = "modified_time";
}
