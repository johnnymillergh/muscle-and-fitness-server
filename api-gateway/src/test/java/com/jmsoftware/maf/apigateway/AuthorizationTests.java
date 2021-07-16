package com.jmsoftware.maf.apigateway;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.AntPathMatcher;

/**
 * Description: AuthorizationTests, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/13/2021 11:27 AM
 **/
@Slf4j
@SpringBootTest
public class AuthorizationTests {
    @Test
    void antPathMatcherTests() {
        val antPathMatcher = new AntPathMatcher();
        val urlMatched = antPathMatcher.match("/**", "/spring-boot-admin/common/app-info");
        log.info("urlMatched: {}", urlMatched);
        Assertions.assertTrue(urlMatched);
    }
}
