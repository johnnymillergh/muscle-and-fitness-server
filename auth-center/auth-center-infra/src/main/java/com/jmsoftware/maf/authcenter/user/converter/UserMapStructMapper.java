package com.jmsoftware.maf.authcenter.user.converter;

import com.jmsoftware.maf.authcenter.user.persistence.User;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

/**
 * Description: UserMapStructMapper, change description here.
 *
 * @author 钟俊 (za-zhongjun), email: jun.zhong@zatech.com, date: 2/5/2022 4:50 PM
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
