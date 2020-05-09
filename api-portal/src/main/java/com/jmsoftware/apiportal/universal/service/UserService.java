package com.jmsoftware.apiportal.universal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.apiportal.universal.domain.GetUserInfoRO;
import com.jmsoftware.apiportal.universal.domain.UserPO;
import com.jmsoftware.apiportal.universal.domain.UserStatus;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <h1>UserService</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019 -06-07 11:42
 */
public interface UserService {
    /**
     * Get user page list
     *
     * @param page pagination object
     * @return user page list
     */
    List<UserPO> getUserPageList(Page page);

    /**
     * Get user by ID and status
     *
     * @param po persistence object
     * @return user po
     */
    UserPO getUserByIdAndStatus(UserPO po);

    /**
     * Get user info (contained role info)
     *
     * @param po persistence object
     * @return user info
     */
    GetUserInfoRO getUserInfo(UserPO po);

    /**
     * Edit user
     *
     * @param po persistence object
     * @return true - edited; false - not edited
     */
    boolean editUserBasicInfo(UserPO po);

    /**
     * Search user by username
     *
     * @param username username
     * @return user user po
     */
    UserPO searchUserByUsername(String username);

    /**
     * Get user list for selection
     *
     * @param page pagination object/
     * @return user list
     */
    List<UserPO> getUserListForSelection(Page page);

    /**
     * Check whether the user account is enabled
     *
     * @param userId   user ID
     * @param username username
     * @return user status
     */
    UserStatus checkUserIsEnabled(Long userId, String username);

    /**
     * Assign role(s) to user
     *
     * @param userId     user ID
     * @param roleIdList role ID list
     */
    void assignRoleToUser(Long userId, List<Long> roleIdList);

    /**
     * <p>Update user&#39;s avatar. When user uploads an avatar picture, we&#39;re going to do the following
     * processes:</p>
     * <ol>
     * <li>Get user&#39;s previous avatar information by username, and delete that avatar if it exists;</li>
     * <li>Upload user&#39;s new avatar to SFTP server;</li>
     * <li>Update user&#39;s avatar information to MySQL database by username.</li>
     * </ol>
     *
     * @param avatar user's avatar file
     * @param po     persistence object
     * @return true - updated avatar successfully; false - updating avatar failed
     * @throws IOException update avatar failed
     */
    boolean updateAvatar(MultipartFile avatar, UserPO po) throws IOException;

    /**
     * Get user's avatar resource
     *
     * @param username username
     * @return user 's avatar
     * @throws IOException IO exception
     */
    ByteArrayResource getUserAvatarResource(String username) throws IOException;

    /**
     * Save user.
     *
     * @param userPo the user po
     */
    void saveUser(UserPO userPo);
}
