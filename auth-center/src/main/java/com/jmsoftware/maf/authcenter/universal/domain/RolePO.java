package com.jmsoftware.maf.authcenter.universal.domain;

import lombok.Data;

import java.util.Date;

/**
 * <h1>RolePO</h1>
 * <p>Persistence class for table `t_role`</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 19:50
 **/
@Data
public class RolePO {
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
     * Create time
     */
    private Date createdTime;
    /**
     * Modify time
     */
    private Date modifiedTime;
}
