package com.jmsoftware.maf.authcenter.role.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.RandomUtil
import cn.hutool.core.util.StrUtil
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.authcenter.role.RoleExcelBean
import com.jmsoftware.maf.authcenter.role.constant.RoleRedisKey.GET_ROLE_LIST_BY_USER_ID
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper
import com.jmsoftware.maf.authcenter.role.persistence.Role
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse
import com.jmsoftware.maf.common.exception.InternalServerException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.function.requireTrue
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import jakarta.validation.Validator
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

/**
 * # RoleDomainServiceImpl
 *
 * Domain Service implementation of Role. (Role)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/10/22 5:25 PM
 */
@Service
class RoleDomainServiceImpl(
    private val mafProjectProperties: MafProjectProperties,
    private val mafConfigurationProperties: MafConfigurationProperties,
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    private val validator: Validator
) : ServiceImpl<RoleMapper, Role>(), RoleDomainService {
    companion object {
        private val logger = logger()
    }

    override fun getRoleList(userId: Long): GetRoleListByUserIdResponse {
        val key = "${mafProjectProperties.projectParentArtifactId}${GET_ROLE_LIST_BY_USER_ID.keyInfixFormat}$userId"
        val hadKey = redisTemplate.hasKey(key)
        if (hadKey) {
            return objectMapper.readValue(redisTemplate.opsForValue()[key], GetRoleListByUserIdResponse::class.java)
        }
        val response = GetRoleListByUserIdResponse()
        response.roleList = getRoleListByUserId(userId)
        redisTemplate.opsForValue()[key, objectMapper.writeValueAsString(response), RandomUtil.randomLong(1, 7)] =
            TimeUnit.DAYS
        return response
    }

    override fun getRoleListByUserId(userId: Long): List<GetRoleListByUserIdSingleResponse> {
        return getBaseMapper().selectRoleListByUserId(userId)
    }

    override fun checkAdmin(roleIdList: List<Long>): Boolean {
        // If roleNameSet is not empty (contains "admin")
        return this.ktQuery()
            .select(Role::name)
            .`in`(Role::id, roleIdList)
            .list()
            .stream()
            .map { item -> item.name }
            .anyMatch { roleName: String ->
                StrUtil.equals(mafConfigurationProperties.superUserRole, roleName)
            }
    }

    override fun getListForExporting(): List<RoleExcelBean> {
        return this.page(Page(1, 500))
            .records
            .stream()
            .map { role: Role -> RoleExcelBean.transformBy(role) }
            .toList()
    }

    override fun validateBeforeAddToBeanList(beanList: List<RoleExcelBean>, bean: RoleExcelBean, index: Int) {
        val constraintViolations = validator.validate(bean)
        if (CollUtil.isNotEmpty(constraintViolations)) {
            logger.warn("Validation failed! beanList: $beanList, bean: $bean, index: $index")
            val first = constraintViolations.first()
            throw IllegalArgumentException("${first.propertyPath} ${first.message}")
        }
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun save(beanList: List<RoleExcelBean>) {
        val roleList = beanList
            .stream()
            .map { roleExcelBean: RoleExcelBean -> RoleExcelBean.transformTo(roleExcelBean) }
            .toList()
        logger.atDebug().log { "Saving roleList: $roleList" }
        requireTrue(this.saveBatch(roleList)) { saved: Boolean ->
            logger.info("Saved role list: $saved")
        }.orElseThrow { InternalServerException("Failed to save roles! Transaction rollback") }
    }
}
