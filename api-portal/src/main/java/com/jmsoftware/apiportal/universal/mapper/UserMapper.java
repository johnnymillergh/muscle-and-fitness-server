package com.jmsoftware.apiportal.universal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.apiportal.universal.domain.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * <h1>UserMapper</h1>
 * <p>CRUD operations for table `t_user`</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-02 17:32
 **/
@Mapper
@Component
public interface UserMapper extends BaseMapper<UserPO> {
    /**
     * Find by username, email or cellphone.
     * <p>
     * TODO: do not retrieve useless fields on result map
     *
     * @param username  Username
     * @param email     Email
     * @param cellphone cellphone number
     * @return User information.
     */
    Optional<UserPO> selectByUsernameOrEmailOrCellphone(@Param("username") String username,
                                                        @Param("email") String email,
                                                        @Param("cellphone") String cellphone);

    /**
     * Count by username.
     *
     * @param username username string
     * @return the count of username occurrence
     */
    Integer countByUsername(String username);

    /**
     * Count by email.
     *
     * @param email email string
     * @return the count of email occurrence
     */
    Integer countByEmail(String email);

    /**
     * Save user
     *
     * @param po A new user.
     * @return Last inserted ID.
     */
    Long save(UserPO po);

    /**
     * Register
     *
     * @param po User info
     * @return Registered user ID
     */
    Long register(UserPO po);

    /**
     * Select user page list
     *
     * @param page pagination object
     * @return user page list
     */
    IPage<UserPO> selectUserPageList(Page<UserPO> page);

    /**
     * Select user by ID and status
     *
     * @param params query params
     * @return user persistence object
     */
    UserPO selectByIdAndStatus(@Param("params") UserPO params);

    /**
     * Update user's basic information by ID
     *
     * @param updated updated user
     * @return affected rows
     */
    int updateUserBasicInfoById(@Param("updated") UserPO updated);

    /**
     * Select user by username
     *
     * @param username username
     * @return user po
     */
    UserPO selectByUsername(@Param("username") String username);

    /**
     * Select user list for lazy selection
     *
     * @param page pagination object
     * @return user page list
     */
    IPage<UserPO> selectUserListForSelection(Page<UserPO> page);

    /**
     * Select user status by ID and username
     *
     * @param id       user ID
     * @param username username
     * @return user status
     */
    Integer selectStatusByIdAndUsername(@Param("id") Long id, @Param("username") String username);

    /**
     * Insert user-role relation
     *
     * @param userId     user ID
     * @param roleIdList role ID list
     * @return affected rows
     */
    int insertUserIdAndRoleIdList(@Param("userId") Long userId, @Param("roleIdList") List<Long> roleIdList);

    /**
     * Update avatar by user's ID
     *
     * @param po persistence object
     * @return affected rows
     */
    int updateAvatarByUsername(@Param("updated") UserPO po);

    /**
     * Select user's ID and avatar by username
     *
     * @param username username
     * @return po
     */
    UserPO selectIdAndAvatarByUsername(@Param("username") String username);
}
