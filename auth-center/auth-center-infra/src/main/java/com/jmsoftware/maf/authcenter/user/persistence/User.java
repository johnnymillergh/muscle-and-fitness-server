package com.jmsoftware.maf.authcenter.user.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;
import static com.baomidou.mybatisplus.annotation.FieldFill.UPDATE;

/**
 * <h1>User</h1>
 * <p>
 * User Persistence object class
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 3:22 PM
 */
@Data
@SuppressWarnings("jol")
@TableName(value = User.TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "user";
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
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_TIME = "created_time";
    public static final String COL_MODIFIED_BY = "modified_by";
    public static final String COL_MODIFIED_TIME = "modified_time";
    public static final String COL_DELETED = "deleted";
    /**
     * Primary key of user
     */
    @TableId(value = COL_ID, type = IdType.AUTO)
    private Long id;
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
    /**
     * Created by
     */
    @TableField(value = COL_CREATED_BY, fill = INSERT)
    private Long createdBy;
    /**
     * Created time
     */
    @TableField(value = COL_CREATED_TIME, fill = INSERT)
    private LocalDateTime createdTime;
    /**
     * Modified by
     */
    @TableField(value = COL_MODIFIED_BY, fill = UPDATE)
    private Long modifiedBy;
    /**
     * Modified time
     */
    @TableField(value = COL_MODIFIED_TIME, fill = UPDATE)
    private LocalDateTime modifiedTime;
    /**
     * Delete flag.
     */
    @TableField(value = COL_DELETED, fill = INSERT)
    private Byte deleted;
}
