package com.jmsoftware.maf.authcenter.user.persistence;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jmsoftware.maf.springcloudstarter.database.BasePersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * <h1>User</h1>
 * <p>
 * User Persistence object class
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 3:22 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = User.TABLE_NAME)
public class User extends BasePersistenceEntity {
    public static final String TABLE_NAME = "user";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_CELLPHONE = "cellphone";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FULL_NAME = "full_name";
    public static final String COL_BIRTHDAY = "birthday";
    public static final String COL_GENDER = "gender";
    public static final String COL_AVATAR = "avatar";
    public static final String COL_STATUS = "status";

    /**
     * Username
     */
    @TableField(value = COL_USERNAME)
    private String username;
    /**
     * Email
     */
    @TableField(value = COL_EMAIL)
    private String email;
    /**
     * Cellphone number
     */
    @TableField(value = COL_CELLPHONE)
    private String cellphone;
    /**
     * Password
     */
    @TableField(value = COL_PASSWORD)
    private String password;
    /**
     * Full name
     */
    @TableField(value = COL_FULL_NAME)
    private String fullName;
    /**
     * Birthday
     */
    @TableField(value = COL_BIRTHDAY)
    private LocalDate birthday;
    /**
     * 26 gender options
     */
    @TableField(value = COL_GENDER)
    private String gender;
    /**
     * User avatar full path on SFTP server
     */
    @TableField(value = COL_AVATAR)
    private String avatar;
    /**
     * Status. 1 - enabled, 2 - disabled
     */
    @TableField(value = COL_STATUS)
    private Byte status;
}
