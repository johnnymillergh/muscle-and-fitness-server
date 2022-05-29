package com.jmsoftware.maf.authcenter.user.converter

import com.jmsoftware.maf.authcenter.user.persistence.User
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

/**
 * Description: UserMapStructMapper, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/5/2022 7:15 PM
 */
@Mapper
interface UserMapStructMapper {
    companion object {
        val INSTANCE: UserMapStructMapper = Mappers.getMapper(UserMapStructMapper::class.java)
    }

    /**
     * User -> response.
     *
     * @param user the user
     * @return the get user by login token response
     */
    fun of(user: User): GetUserByLoginTokenResponse
}
