package com.jmsoftware.authcenter.user.mapper;

import com.jmsoftware.authcenter.user.entity.UserPersistence;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <h1>UserMapper</h1>
 * <p>
 * Mapper of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 5 /10/20 12:17 PM
 */
@Mapper
public interface UserMapper {
    /**
     * Select by id user persistence.
     *
     * @param id the id
     * @return the user persistence
     */
    UserPersistence selectById(Long id);

    /**
     * Select all by limit list.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    List<UserPersistence> selectAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * Query all list.
     *
     * @param userPersistence the user persistence
     * @return the list
     */
    List<UserPersistence> selectAll(UserPersistence userPersistence);

    /**
     * Insert int.
     *
     * @param userPersistence the user persistence
     * @return the int
     */
    int insert(UserPersistence userPersistence);

    /**
     * Update int.
     *
     * @param userPersistence the user persistence
     * @return the int
     */
    int update(UserPersistence userPersistence);

    /**
     * Delete by id int.
     *
     * @param id the id
     * @return the int
     */
    int deleteById(Long id);

    /**
     * Select user by username or email user persistence.
     *
     * @param loginToken the login token
     * @return the user persistence
     */
    UserPersistence selectUserByUsernameOrEmail(String loginToken);
}
