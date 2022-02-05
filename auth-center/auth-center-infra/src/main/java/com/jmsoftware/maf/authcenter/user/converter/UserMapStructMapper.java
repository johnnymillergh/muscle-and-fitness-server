package com.jmsoftware.maf.authcenter.user.converter;

import com.jmsoftware.maf.authcenter.user.persistence.User;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

/**
 * Description: UserMapStructMapper, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/5/2022 7:15 PM
 **/
@Mapper
public interface UserMapStructMapper {
    UserMapStructMapper INSTANCE = getMapper(UserMapStructMapper.class);

    /**
     * User -> response.
     *
     * @param user the user
     * @return the get user by login token response
     */
    GetUserByLoginTokenResponse of(User user);
}
