package com.jmsoftware.maf.authcenter.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmsoftware.maf.authcenter.user.entity.persistence.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <h1>UserMapper</h1>
 * <p>
 * Mapper of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 5 /10/20 12:17 PM
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
