package com.jmsoftware.apiportal.universal.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <h1>GetUserInfoRO</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-06-30 11:01
 **/
@Data
public class GetUserInfoRO {
    private Long id;
    private String username;
    private String email;
    private String cellphone;
    private String fullName;
    private Date birthday;
    private String gender;
    private Integer status;
    private List<UsersRole> usersRoles = new ArrayList<>();

    @Data
    public static class UsersRole {
        private Long roleId;
        private String roleName;
        private String roleDescription;
    }
}
