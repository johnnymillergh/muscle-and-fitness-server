package com.jmsoftware.maf.common.domain.authcenter.role;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * <h1>GetRoleListByUserIdResponse</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/10/20 10:55 PM
 **/
@Data
public class GetRoleListByUserIdResponse {
    private final List<Role> roleList = new LinkedList<>();

    @Data
    public static class Role {
        private Long id;
        private String name;
    }
}
