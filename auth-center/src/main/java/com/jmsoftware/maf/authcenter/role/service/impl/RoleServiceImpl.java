package com.jmsoftware.maf.authcenter.role.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.validation.ValidationUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.authcenter.role.entity.RoleExcelImport;
import com.jmsoftware.maf.authcenter.role.entity.constant.RoleRedisKey;
import com.jmsoftware.maf.authcenter.role.entity.persistence.Role;
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper;
import com.jmsoftware.maf.authcenter.role.service.RoleService;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse;
import com.jmsoftware.maf.springcloudstarter.annotation.ExcelColumn;
import com.jmsoftware.maf.springcloudstarter.configuration.MafConfiguration;
import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <h1>RoleServiceImpl</h1>
 * <p>
 * Service implementation of Role.(Role)
 *
 * @author Johnny Miller (锺俊)
 * @date 2020-05-10 22:39:50
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private final MafProjectProperty mafProjectProperty;
    private final MafConfiguration mafConfiguration;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows({JsonProcessingException.class})
    public GetRoleListByUserIdResponse getRoleList(@NotNull Long userId) {
        val key = String.format(this.mafProjectProperty.getProjectParentArtifactId()
                                        + RoleRedisKey.GET_ROLE_LIST_BY_USER_ID.getKeyInfixFormat(), userId);
        val hasKey = this.redisTemplate.hasKey(key);
        if (BooleanUtil.isTrue(hasKey)) {
            return this.objectMapper.readValue(this.redisTemplate.opsForValue().get(key),
                                               GetRoleListByUserIdResponse.class);
        }
        val response = new GetRoleListByUserIdResponse();
        response.setRoleList(this.getRoleListByUserId(userId));
        this.redisTemplate.opsForValue().set(key, this.objectMapper.writeValueAsString(response),
                                             RandomUtil.randomLong(1, 7),
                                             TimeUnit.DAYS);
        return response;
    }

    @Override
    public List<GetRoleListByUserIdSingleResponse> getRoleListByUserId(@NonNull Long userId) {
        return this.getBaseMapper().selectRoleListByUserId(userId);
    }

    @Override
    public boolean checkAdmin(@NotEmpty List<@NotNull Long> roleIdList) {
        val wrapper = Wrappers.lambdaQuery(Role.class);
        wrapper.select(Role::getName)
                .in(Role::getId, roleIdList);
        val roleList = this.list(wrapper);
        val roleNameSet = roleList
                .stream()
                .map(Role::getName)
                .filter(roleName -> StrUtil.equals(this.mafConfiguration.getSuperUserRole(), roleName))
                .collect(Collectors.toSet());
        // If roleNameSet is not empty (contains "admin")
        return CollUtil.isNotEmpty(roleNameSet);
    }

    @Override
    public ResponseEntity<StreamingResponseBody> downloadRoleStat() {
        val rolePage = new Page<Role>(1, 500);
        this.page(rolePage);
        val roleExcelImportList = rolePage
                .getRecords()
                .stream()
                .map(RoleExcelImport::transformBy)
                .collect(Collectors.toList());
        val excelWriter = new ExcelWriter(true);
        Field[] declaredFields = RoleExcelImport.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ExcelColumn annotation = declaredField.getAnnotation(ExcelColumn.class);
            excelWriter.addHeaderAlias(declaredField.getName(), annotation.description());
        }
        excelWriter.write(roleExcelImportList);
        excelWriter.setFreezePane(1);
        excelWriter.autoSizeColumnAll();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(ROLE_TEMPLATE_EXCEL).build().toString())
                .body(outputStream -> excelWriter.flush(outputStream, true));
    }

    @Override
    public boolean validateBeforeAddToBeanList(List<RoleExcelImport> beanList, RoleExcelImport bean, int index) {
        val beanValidationResult = ValidationUtil.warpValidate(bean);
        if (!beanValidationResult.isSuccess()) {
            log.warn("Validation failed! beanList: {}, bean: {}, index: {}", beanList, bean, index);
            val firstErrorMessage = CollUtil.getFirst(beanValidationResult.getErrorMessages());
            throw new IllegalArgumentException(
                    String.format("%s %s", firstErrorMessage.getPropertyName(), firstErrorMessage.getMessage()));
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(@NotEmpty List<@Valid RoleExcelImport> beanList) {
        val roleList = beanList.stream().map(RoleExcelImport::transformTo).collect(Collectors.toList());
        val saved = this.saveBatch(roleList);
        if (!saved) {
            log.error("Cannot save batch role list. {}", roleList);
        }
    }
}
