package com.jmsoftware.maf.authcenter.user.service.impl

import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.RandomUtil
import cn.hutool.json.JSONUtil
import com.baomidou.mybatisplus.core.metadata.OrderItem.desc
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.jmsoftware.maf.authcenter.security.service.JwtService
import com.jmsoftware.maf.authcenter.user.constant.UserRedisKey
import com.jmsoftware.maf.authcenter.user.converter.UserMapStructMapper
import com.jmsoftware.maf.authcenter.user.mapper.UserMapper
import com.jmsoftware.maf.authcenter.user.payload.GetUserPageListPayload
import com.jmsoftware.maf.authcenter.user.payload.GetUserStatusPayload
import com.jmsoftware.maf.authcenter.user.persistence.User
import com.jmsoftware.maf.authcenter.user.service.UserDomainService
import com.jmsoftware.maf.authcenter.user.service.UserRoleDomainService
import com.jmsoftware.maf.common.bean.PageResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.user.*
import com.jmsoftware.maf.common.exception.SecurityException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import com.jmsoftware.maf.springcloudstarter.util.currentUsername
import org.springframework.cache.annotation.CacheConfig
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit
import jakarta.servlet.http.HttpServletRequest

/**
 * # UserDomainServiceImpl
 *
 * Service implementation of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (锺俊), date 2020-05-10 12:08:28
 */
@Service
@CacheConfig(cacheNames = ["user-service-cache"])
class UserDomainServiceImpl(
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtService: JwtService,
    private val messageSource: MessageSource,
    private val mafProjectProperties: MafProjectProperties,
    private val redisTemplate: RedisTemplate<String, String>,
    private val userRoleDomainService: UserRoleDomainService,
    private val mafConfigurationProperties: MafConfigurationProperties,
) : ServiceImpl<UserMapper, User>(), UserDomainService {
    companion object {
        private val logger = logger()
    }

    override fun getUserByLoginToken(loginToken: String): GetUserByLoginTokenResponse? {
        val key =
            "${mafProjectProperties.projectParentArtifactId}${UserRedisKey.GET_USER_BY_LOGIN_TOKEN.keyInfixFormat}$loginToken"
        val hasKey = redisTemplate.hasKey(key)
        if (hasKey) {
            return JSONUtil.toBean(redisTemplate.opsForValue()[key], GetUserByLoginTokenResponse::class.java)
        }
        val user = this.ktQuery().eq(User::username, loginToken).one()
        if (ObjectUtil.isNull(user)) {
            return null
        }
        val response = UserMapStructMapper.INSTANCE.of(user)
        redisTemplate.opsForValue()[key, JSONUtil.toJsonStr(response), RandomUtil.randomLong(1, 7)] = TimeUnit.DAYS
        return response
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun saveUserForSignup(payload: SignupPayload): SignupResponse {
        val user = User()
        user.username = payload.username
        user.email = payload.email
        user.password = bCryptPasswordEncoder.encode(payload.password)
        user.status = UserStatus.ENABLED.value
        save(user)
        logger.warn("Saved user for signup, going to assign guest role to user. $user")
        userRoleDomainService.assignRoleByRoleName(user, mafConfigurationProperties.guestUserRole)
        val response = SignupResponse()
        response.userId = user.id
        return response
    }

    override fun login(payload: LoginPayload): LoginResponse {
        val user = getUserByLoginToken(payload.loginToken) ?: throw SecurityException(HttpStatus.UNAUTHORIZED)
        logger.info("User login: $user")
        val matched = bCryptPasswordEncoder.matches(payload.password, user.password)
        if (!matched) {
            throw SecurityException(HttpStatus.UNAUTHORIZED)
        }
        val jwt = jwtService.createJwt(payload.rememberMe, user.id!!, user.username!!, listOf(), listOf())
        val response = LoginResponse()
        response.greeting = messageSource.getMessage(
            "greeting",
            null,
            LocaleContextHolder.getLocale()
        )
        response.jwt = jwt
        return response
    }

    override fun logout(request: HttpServletRequest): Boolean {
        jwtService.invalidateJwt(request)
        return true
    }

    override fun getUserStatus(payload: GetUserStatusPayload): String {
        logger.info("Current username: ${currentUsername()}")
        return payload.status.toString()
    }

    override fun getUserPageList(payload: GetUserPageListPayload): PageResponseBodyBean<User> {
        val page = Page<User>(payload.currentPage.toLong(), payload.pageSize.toLong())
            .apply { this.orders = listOf(desc(payload.orderBy)) }
        this.ktQuery().apply {
            payload.username?.let {
                if (it.isNotBlank()) {
                    this.like(User::username, payload.username)
                }
            }
        }.page(page)
        return PageResponseBodyBean.ofSuccess(page.records, page.total)
    }
}
