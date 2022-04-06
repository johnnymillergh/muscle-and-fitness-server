package com.jmsoftware.maf.common.domain.authcenter.role;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <h1>GetRoleListByUserIdResponse</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/10/20 10:55 PM
 **/
@Data
public class GetRoleListByUserIdResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -8462678958191383914L;

    private List<GetRoleListByUserIdSingleResponse> roleList;
}
