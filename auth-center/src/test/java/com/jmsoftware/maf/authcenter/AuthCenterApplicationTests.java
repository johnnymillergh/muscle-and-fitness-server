package com.jmsoftware.maf.authcenter;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.authcenter.universal.domain.UserPO;
import com.jmsoftware.maf.authcenter.universal.domain.UserPrincipal;
import com.jmsoftware.maf.authcenter.universal.service.JwtService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;

/**
 * Description: AuthCenterApplicationTests.
 *
 * @author 钟俊 (zhongjun), email: zhongjun@toguide.cn, date: 12/21/2020 3:08 PM
 */
@Slf4j
@SpringBootTest
class AuthCenterApplicationTests {
    @Autowired
    private JwtService jwtService;

    @Test
    @SneakyThrows
    void mockLogin() {
        UserPO userPO = new UserPO();
        userPO.setId(1L);
        userPO.setUsername("ijohnnymiller");
        val authenticationToken = new UsernamePasswordAuthenticationToken(
                UserPrincipal.create(userPO, new ArrayList<>(), new ArrayList<>()), 12345678);
        String jwt = jwtService.createJwt(authenticationToken, false);
        log.info("Generated JWT: {}", jwt);
        Assertions.assertTrue(StrUtil.isNotBlank(jwt));
    }
}
