package com.jmsoftware.maf.reactivespringcloudstarter

import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.reactivespringcloudstarter.configuration.WebClientConfiguration
import com.jmsoftware.maf.reactivespringcloudstarter.configuration.WebFluxConfiguration
import com.jmsoftware.maf.reactivespringcloudstarter.controller.CommonController
import com.jmsoftware.maf.reactivespringcloudstarter.filter.AccessLogFilter
import com.jmsoftware.maf.reactivespringcloudstarter.helper.IpHelper
import com.jmsoftware.maf.reactivespringcloudstarter.helper.SpringBootStartupHelper
import com.jmsoftware.maf.reactivespringcloudstarter.property.JwtConfigurationProperties
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafConfigurationProperties
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafProjectProperties
import com.jmsoftware.maf.reactivespringcloudstarter.redis.RedisConfiguration
import com.jmsoftware.maf.reactivespringcloudstarter.service.CommonService
import com.jmsoftware.maf.reactivespringcloudstarter.service.impl.CommonServiceImpl
import com.jmsoftware.maf.reactivespringcloudstarter.util.ResponseUtil
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment

/**
 * # MafReactiveAutoConfiguration
 *
 * Description: MafReactiveAutoConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/17/22 8:17 AM
 */
@AutoConfiguration
@AutoConfigureOrder(Int.MIN_VALUE)
@EnableConfigurationProperties(
    MafConfigurationProperties::class,
    MafProjectProperties::class,
    JwtConfigurationProperties::class
)
@Import(RedisConfiguration::class, WebFluxConfiguration::class, WebClientConfiguration::class)
class MafReactiveAutoConfiguration {
    companion object {
        private val log = logger()
    }

    @PostConstruct
    fun postConstruct() {
        log.warn("Post construction of [${this.javaClass.simpleName}] is done. About to inject beans. Auto configure order: ${Int.MIN_VALUE}")
    }

    @Bean
    fun requestFilter(mafConfigurationProperties: MafConfigurationProperties): AccessLogFilter =
        AccessLogFilter(mafConfigurationProperties).also {
            log.warn("Initial bean: `${AccessLogFilter::class.java.simpleName}`")
        }

    @Bean
    fun ipHelper(environment: Environment): IpHelper = IpHelper(environment).also {
        log.warn("Initial bean: `${IpHelper::class.java.simpleName}`")
    }

    @Bean
    fun springBootStartupHelper(
        mafProjectProperties: MafProjectProperties,
        ipHelper: IpHelper,
        applicationContext: ApplicationContext
    ): SpringBootStartupHelper = SpringBootStartupHelper(mafProjectProperties, ipHelper, applicationContext).also {
        log.warn("Initial bean: `${SpringBootStartupHelper::class.java.simpleName}`")
    }

    @Bean
    fun commonService(mafProjectProperties: MafProjectProperties): CommonService =
        CommonServiceImpl(mafProjectProperties).also {
            log.warn("Initial bean: `${CommonService::class.java.simpleName}`")
        }

    @Bean
    fun commonController(commonService: CommonService): CommonController = CommonController(commonService).also {
        log.warn("Initial bean: `${CommonController::class.java.simpleName}`")
    }

    @Bean
    fun responseUtil(objectMapper: ObjectMapper): ResponseUtil = ResponseUtil(objectMapper).also {
        log.warn("Initial bean: `${ResponseUtil::class.java.simpleName}`")
    }
}
