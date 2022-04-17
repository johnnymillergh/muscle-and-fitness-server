package com.jmsoftware.maf.authcenter.user.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.jmsoftware.maf.authcenter.user.persistence.User
import org.apache.ibatis.annotations.Mapper

/**
 * # UserMapper
 *
 * Mapper of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:45 PM
 */
@Mapper
interface UserMapper : BaseMapper<User>
