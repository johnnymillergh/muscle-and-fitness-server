package com.jmsoftware.maf.apigateway

import com.jmsoftware.maf.common.util.logger
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.util.AntPathMatcher

/**
 * # AuthorizationTests
 *
 * Description: AuthorizationTests, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:15 PM
 */
internal class AuthorizationTests {
    companion object {
        private val log = logger()
    }

    @Test
    fun antPathMatcherTests() {
        val antPathMatcher = AntPathMatcher()
        val urlMatched1 = antPathMatcher.match("/spring-boot-admin/**", "/spring-boot-admin/common/app-info")
        log.info("urlMatched1: $urlMatched1")
        assertTrue(urlMatched1)
        val urlMatched2 = antPathMatcher.match("/**", "/spring-boot-admin/common/app-info")
        log.info("urlMatched2: $urlMatched2")
        assertTrue(urlMatched2)
    }
}
