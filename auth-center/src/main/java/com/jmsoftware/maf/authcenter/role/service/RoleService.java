package com.jmsoftware.maf.authcenter.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmsoftware.maf.authcenter.role.entity.RolePersistence;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * <h1>RoleService</h1>
 * <p>
 * Service of Role.(Role)
 *
 * @author Johnny Miller (鍾俊)
 * @date 2020 -05-10 22:39:49
 */
@Validated
public interface RoleService extends IService<RolePersistence> {
    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    GetRoleListByUserIdResponse getRoleList(Long userId);

    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    List<RolePersistence> getRoleListByUserId(@NonNull Long userId);
}
