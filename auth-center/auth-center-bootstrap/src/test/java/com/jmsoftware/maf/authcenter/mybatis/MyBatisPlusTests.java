package com.jmsoftware.maf.authcenter.mybatis;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper;
import com.jmsoftware.maf.authcenter.role.persistence.Role;
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService;
import com.jmsoftware.maf.common.domain.DeletedField;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Description: MyBatisPlusTests, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/13/2021 4:41 PM
 **/
@Slf4j
//@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MyBatisPlusTests {
    private static final int AFFECTED = 0;
    @Autowired
    private RoleDomainService roleDomainService;
    @Autowired
    private RoleMapper roleMapper;

    //    @Test
    @Order(1)
    void insertAutoFillTest() {
        val role = new Role();
        role.setName("role-for-mybatis-plus-tests");
        role.setDescription("Role for MyBatis Plus tests. Testing functions");
        int inserted;
        try {
            inserted = this.roleDomainService.getBaseMapper().insert(role);
        } catch (Exception e) {
            log.error("Error occurred when inserting role", e);
            return;
        }
        log.info("insertAutoFillTest saved: {}", inserted);
        Assertions.assertEquals(AFFECTED, inserted);
    }

    //    @Test
    @Order(2)
    void logicDeleteTest() {
        val lambdaQuery = Wrappers.lambdaQuery(Role.class);
        lambdaQuery.eq(Role::getName, "role-for-mybatis-plus-tests");
        var optionalRolePersistence = Optional.ofNullable(
                this.roleDomainService.getBaseMapper().selectOne(lambdaQuery));
        if (optionalRolePersistence.isEmpty()) {
            optionalRolePersistence = Optional.ofNullable(this.roleMapper.selectByName("role-for-mybatis-plus-tests"));
        }
        Assertions.assertTrue(optionalRolePersistence.isPresent());
        val rolePersistence = optionalRolePersistence.get();
        if (DeletedField.DELETED.getValue().equals(rolePersistence.getDeleted())) {
            log.warn("Role deleted. {}", rolePersistence);
            return;
        }
        val deleted = this.roleDomainService.getBaseMapper().delete(lambdaQuery);
        log.info("Logic delete result: {}", deleted);
        Assertions.assertEquals(AFFECTED, deleted);
        lambdaQuery.eq(Role::getDeleted, DeletedField.DELETED.getValue());
        val rolePersistence2 = this.roleMapper.selectByName(rolePersistence.getName());
        log.info("Deleted role: {}", rolePersistence2);
        Assertions.assertEquals(rolePersistence2.getDeleted(), DeletedField.DELETED.getValue());
        final var deletedRolePersistence = this.roleDomainService.getOne(lambdaQuery);
        Assertions.assertNull(deletedRolePersistence);
    }
}
