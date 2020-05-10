package com.jmsoftware.authcenter.user.service;

import com.jmsoftware.authcenter.user.entity.UserPersistence;
import com.jmsoftware.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import com.jmsoftware.common.domain.authcenter.user.SaveUserForRegisteringPayload;
import com.jmsoftware.common.domain.authcenter.user.SaveUserForRegisteringResponse;

import java.util.List;

/**
 * <h1>UserService</h1>
 * <p>
 * Service of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 5 /10/20 12:31 PM
 */
public interface UserService {
    /**
     * Query by id user persistence.
     *
     * @param id the id
     * @return the user persistence
     */
    UserPersistence queryById(Long id);

    /**
     * Query all by limit list.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    List<UserPersistence> queryAllByLimit(int offset, int limit);

    /**
     * Insert user persistence.
     *
     * @param userPersistence the user persistence
     * @return the user persistence
     */
    UserPersistence insert(UserPersistence userPersistence);

    /**
     * Update user persistence.
     *
     * @param userPersistence the user persistence
     * @return the user persistence
     */
    UserPersistence update(UserPersistence userPersistence);

    /**
     * Delete by id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean deleteById(Long id);

    /**
     * Gets user by login token.
     *
     * @param loginToken the login token
     * @return the user by login token
     */
    GetUserByLoginTokenResponse getUserByLoginToken(String loginToken);

    /**
     * Save user for registering save user for registering response.
     *
     * @param payload the payload
     * @return the save user for registering response
     */
    SaveUserForRegisteringResponse saveUserForRegistering(SaveUserForRegisteringPayload payload);
}
