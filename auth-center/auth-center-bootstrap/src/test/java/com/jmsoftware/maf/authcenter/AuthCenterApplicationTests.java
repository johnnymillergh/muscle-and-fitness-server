package com.jmsoftware.maf.authcenter;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.authcenter.security.service.JwtService;
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;

/**
 * Description: AuthCenterApplicationTests.
 *
 * @author 钟俊 (zhongjun), email: zhongjun@toguide.cn, date: 12/21/2020 3:08 PM
 * @see <a href='https://www.baeldung.com/spring-boot-testing'>Testing in Spring Boot</a>
 */
@Slf4j
//@SpringBootTest
class AuthCenterApplicationTests {
    @Autowired
    private JwtService jwtService;

//    @Test
    @SneakyThrows
    void mockLogin() {
        GetUserByLoginTokenResponse user = new GetUserByLoginTokenResponse();
        user.setId(1L);
        user.setUsername("ijohnnymiller");
        val authenticationToken = new UsernamePasswordAuthenticationToken(
                UserPrincipal.create(user, new ArrayList<>(), new ArrayList<>()), 12345678);
        String jwt = this.jwtService.createJwt(authenticationToken, false);
        log.info("Generated JWT: {}", jwt);
        Assertions.assertTrue(StrUtil.isNotBlank(jwt));
    }
}
