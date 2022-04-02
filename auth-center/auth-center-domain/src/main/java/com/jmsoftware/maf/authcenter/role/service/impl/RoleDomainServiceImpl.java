package com.jmsoftware.maf.authcenter.role.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.validation.ValidationUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.authcenter.role.RoleExcelBean;
import com.jmsoftware.maf.authcenter.role.constant.RoleRedisKey;
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper;
import com.jmsoftware.maf.authcenter.role.persistence.Role;
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse;
import com.jmsoftware.maf.common.exception.InternalServerException;
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties;
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.jmsoftware.maf.springcloudstarter.function.BooleanCheck.requireTrue;
import static com.jmsoftware.maf.springcloudstarter.function.Slf4j.lazyDebug;

/**
 * <h1>RoleDomainServiceImpl</h1>
 * <p>
 * Service implementation of Role.(Role)
 *
 * @author Johnny Miller (锺俊)
 * @date 2020-05-10 22:39:50
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleDomainServiceImpl
        extends ServiceImpl<RoleMapper, Role>
        implements RoleDomainService {
    private final MafProjectProperties mafProjectProperties;
    private final MafConfigurationProperties mafConfigurationProperties;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows({JsonProcessingException.class})
    public GetRoleListByUserIdResponse getRoleList(@NotNull Long userId) {
        val key = String.format(String.format("%s%s", this.mafProjectProperties.getProjectParentArtifactId(),
                                              RoleRedisKey.GET_ROLE_LIST_BY_USER_ID.getKeyInfixFormat()), userId);
        val hasKey = this.redisTemplate.hasKey(key);
        if (BooleanUtil.isTrue(hasKey)) {
            return this.objectMapper.readValue(this.redisTemplate.opsForValue().get(key),
                                               GetRoleListByUserIdResponse.class);
        }
        val response = new GetRoleListByUserIdResponse();
        response.setRoleList(this.getRoleListByUserId(userId));
        this.redisTemplate.opsForValue()
                .set(key, this.objectMapper.writeValueAsString(response), RandomUtil.randomLong(1, 7), TimeUnit.DAYS);
        return response;
    }

    @Override
    public List<GetRoleListByUserIdSingleResponse> getRoleListByUserId(@NonNull Long userId) {
        return this.getBaseMapper().selectRoleListByUserId(userId);
    }

    @Override
    public boolean checkAdmin(@NotEmpty List<@NotNull Long> roleIdList) {
        // If roleNameSet is not empty (contains "admin")
        return this.list(Wrappers.lambdaQuery(Role.class).select(Role::getName).in(Role::getId, roleIdList))
                .stream()
                .map(Role::getName)
                .anyMatch(roleName -> StrUtil.equals(this.mafConfigurationProperties.getSuperUserRole(), roleName));
    }

    @Override
    public List<RoleExcelBean> getListForExporting() {
        return this.page(new Page<>(1, 500))
                .getRecords()
                .stream()
                .map(RoleExcelBean::transformBy)
                .collect(Collectors.toList());
    }

    @Override
    public void validateBeforeAddToBeanList(List<RoleExcelBean> beanList, RoleExcelBean bean, int index) throws IllegalArgumentException {
        val beanValidationResult = ValidationUtil.warpValidate(bean);
        if (!beanValidationResult.isSuccess()) {
            log.warn("Validation failed! beanList: {}, bean: {}, index: {}", beanList, bean, index);
            val firstErrorMessage = CollUtil.getFirst(beanValidationResult.getErrorMessages());
            throw new IllegalArgumentException(
                    format("{} {}", firstErrorMessage.getPropertyName(), firstErrorMessage.getMessage()));
        }
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Throwable.class)
    public void save(@NotEmpty List<@Valid RoleExcelBean> beanList) {
        val roleList = beanList.stream().map(RoleExcelBean::transformTo).collect(Collectors.toList());
        lazyDebug(log, () -> format("Saving roleList: {}", roleList));
        requireTrue(this.saveBatch(roleList), saved -> log.info("Saved role list: {}", saved))
                .orElseThrow(() -> new InternalServerException("Failed to save roles! Transaction rollback"));
    }
}