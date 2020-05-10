package com.jmsoftware.authcenter.role.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Role.(Role) Persistence object class
 *
 * @author Johnny Miller (鍾俊)
 * @since 2020-05-10 22:39:45
 */
@Data
public class RolePersistence implements Serializable {
    private static final long serialVersionUID = -81197803690669820L;
    /**
    * Primary key
    */
    private Long id;
    /**
    * Role name
    */
    private String name;
    /**
    * Role description
    */
    private String description;
    /**
    * Created time
    */
    private Date createdTime;
    /**
    * Modified time
    */
    private Date modifiedTime;
}
