package com.jmsoftware.maf.authcenter.mybatis;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jmsoftware.maf.authcenter.role.entity.RolePersistence;
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper;
import com.jmsoftware.maf.authcenter.role.service.RoleService;
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
        val rolePersistence = new RolePersistence();
        rolePersistence.setName("role-for-mybatis-plus-tests");
        rolePersistence.setDescription("Role for MyBatis Plus tests. Testing functions");
        int inserted;
        try {
            inserted = roleMapper.insert(rolePersistence);
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
        val lambdaQuery = Wrappers.lambdaQuery(RolePersistence.class);
        lambdaQuery.eq(RolePersistence::getName, "role-for-mybatis-plus-tests");
        val optionalRolePersistence = Optional.ofNullable(roleMapper.selectOne(lambdaQuery));
        if (optionalRolePersistence.isPresent()) {
            val deleted = roleMapper.delete(lambdaQuery);
            log.info("Logic delete result: {}", deleted);
            Assertions.assertEquals(deleted, 1);
            val rolePersistence = roleMapper.selectOne(lambdaQuery);
            log.info("DeleteField role: {}", rolePersistence);
            Assertions.assertNotEquals(rolePersistence.getCreatedTime(), rolePersistence.getModifiedTime());
            return;
        }
        log.warn("Role not found! {}", lambdaQuery.getEntity());
    }
}
