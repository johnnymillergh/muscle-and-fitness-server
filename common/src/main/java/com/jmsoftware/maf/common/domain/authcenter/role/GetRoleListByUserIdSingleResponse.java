package com.jmsoftware.maf.common.domain.authcenter.role;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Description: GetRoleListByUserIdSingleResponse, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/29/2021 10:17 AM
 **/
@Data
public class GetRoleListByUserIdSingleResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 3758434255123050684L;

    private Long id;
    private String name;
}
