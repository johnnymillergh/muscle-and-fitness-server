package com.jmsoftware.apiportal.universal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.apiportal.universal.domain.PermissionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <h1>PermissionMapper</h1>
 * <p>CRUD operations for table `t_permission`</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-02 17:52
 **/
@Mapper
@Component
public interface PermissionMapper extends BaseMapper<PermissionPO> {
    /**
     * Save a permission
     *
     * @param po persistence object
     * @return permission's ID
     */
    Long insertPermission(PermissionPO po);

    /**
     * Select permission list by role id
     *
     * @param ids Role's id list
     * @return Permission list
     */
    List<PermissionPO> selectByRoleIdList(List<Long> ids);

    /**
     * Find permission by URL.
     *
     * @param url URL
     * @return permission
     */
    Long countInUseApiByUrl(@Param("url") String url);

    /**
     * Find APIs by URL prefix.
     *
     * @param urlPrefix URL prefix
     * @return permissions
     */
    List<PermissionPO> selectApisByUrlPrefix(String urlPrefix);

    /**
     * Select API page list
     *
     * @param page pagination object
     * @return API list
     */
    IPage<PermissionPO> selectApiPageList(Page page);
}
