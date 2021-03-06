package com.jmsoftware.maf.authcenter.mybatis;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jmsoftware.maf.authcenter.role.entity.persistence.Role;
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper;
import com.jmsoftware.maf.authcenter.role.service.RoleService;
import com.jmsoftware.maf.common.domain.DeletedField;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

/**
 * Description: MyBatisPlusTests, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/13/2021 4:41 PM
 **/
@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyBatisPlusTests {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMapper roleMapper;

    @Test
    @Order(1)
    void insertAutoFillTest() {
        val role = new Role();
        role.setName("role-for-mybatis-plus-tests");
        role.setDescription("Role for MyBatis Plus tests. Testing functions");
        int inserted;
        try {
            inserted = roleService.getBaseMapper().insert(role);
        } catch (Exception e) {
            log.error("Error occurred when inserting role", e);
            return;
        }
        log.info("insertAutoFillTest saved: {}", inserted);
        Assertions.assertEquals(inserted, 1);
    }

    @Test
    @Order(2)
    void logicDeleteTest() {
        val lambdaQuery = Wrappers.lambdaQuery(Role.class);
        lambdaQuery.eq(Role::getName, "role-for-mybatis-plus-tests");
        var optionalRolePersistence = Optional.ofNullable(roleService.getBaseMapper().selectOne(lambdaQuery));
        if (optionalRolePersistence.isEmpty()) {
            optionalRolePersistence = Optional.ofNullable(roleMapper.selectByName("role-for-mybatis-plus-tests"));
        }
        Assertions.assertTrue(optionalRolePersistence.isPresent());
        val rolePersistence = optionalRolePersistence.get();
        if (DeletedField.DELETED.getValue().equals(rolePersistence.getDeleted())) {
            log.warn("Role deleted. {}", rolePersistence);
            return;
        }
        val deleted = roleService.getBaseMapper().delete(lambdaQuery);
        log.info("Logic delete result: {}", deleted);
        Assertions.assertEquals(deleted, 1);
        lambdaQuery.eq(Role::getDeleted, DeletedField.DELETED.getValue());
        val rolePersistence2 = roleMapper.selectByName(rolePersistence.getName());
        log.info("Deleted role: {}", rolePersistence2);
        Assertions.assertEquals(rolePersistence2.getDeleted(), DeletedField.DELETED.getValue());
        final var deletedRolePersistence = roleService.getOne(lambdaQuery);
        Assertions.assertNull(deletedRolePersistence);
    }
}
