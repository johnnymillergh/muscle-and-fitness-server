package com.jmsoftware.maf.common.domain.authcenter.permission;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.LinkedList;
import java.util.List;

/**
 * <h1>GetPermissionListByRoleIdListPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/11/20 8:38 AM
 **/
@Data
public class GetPermissionListByRoleIdListPayload {
    /**
     * The Role id list.
     */
    @NotEmpty
    private final List<Long> roleIdList = new LinkedList<>();
}
