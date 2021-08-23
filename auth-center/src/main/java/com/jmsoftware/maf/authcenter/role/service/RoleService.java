package com.jmsoftware.maf.authcenter.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmsoftware.maf.authcenter.role.entity.RoleExcelBean;
import com.jmsoftware.maf.authcenter.role.entity.persistence.Role;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <h1>RoleService</h1>
 * <p>
 * Service of Role.(Role)
 *
 * @author Johnny Miller (锺俊)
 * @date 2020 -05-10 22:39:49
 */
@Validated
public interface RoleService extends IService<Role> {
    String ROLE_TEMPLATE_EXCEL = "role-stat.xlsx";

    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    GetRoleListByUserIdResponse getRoleList(@NotNull Long userId);

    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    List<GetRoleListByUserIdSingleResponse> getRoleListByUserId(@NonNull Long userId);

    /**
     * Check admin boolean.
     *
     * @param roleIdList the role id list
     * @return the boolean
     */
    boolean checkAdmin(@NotEmpty List<@NotNull Long> roleIdList);

    /**
     * Gets list for exporting.
     *
     * @return the list for exporting
     */
    List<RoleExcelBean> getListForExporting();

    /**
     * Validate before add to bean list boolean.
     *
     * @param beanList the bean list
     * @param bean     the bean
     * @param index    the index
     * @throws IllegalArgumentException the illegal argument exception
     */
    void validateBeforeAddToBeanList(List<RoleExcelBean> beanList, RoleExcelBean bean, int index) throws IllegalArgumentException;

    /**
     * Save.
     *
     * @param beanList the bean list
     */
    void save(@NotEmpty List<@Valid RoleExcelBean> beanList);
}
