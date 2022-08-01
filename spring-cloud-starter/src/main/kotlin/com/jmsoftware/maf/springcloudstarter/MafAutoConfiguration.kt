package com.jmsoftware.maf.springcloudstarter

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException
import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.aspect.*
import com.jmsoftware.maf.springcloudstarter.configuration.*
import com.jmsoftware.maf.springcloudstarter.controller.CommonController
import com.jmsoftware.maf.springcloudstarter.controller.GlobalErrorController
import com.jmsoftware.maf.springcloudstarter.controller.HttpApiResourceRemoteApiController
import com.jmsoftware.maf.springcloudstarter.controller.RedirectController
import com.jmsoftware.maf.springcloudstarter.database.MyBatisPlusConfiguration
import com.jmsoftware.maf.springcloudstarter.elasticsearch.ElasticsearchConfiguration
import com.jmsoftware.maf.springcloudstarter.filter.AccessLogFilter
import com.jmsoftware.maf.springcloudstarter.helper.HttpApiScanHelper
import com.jmsoftware.maf.springcloudstarter.helper.IpHelper
import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper
import com.jmsoftware.maf.springcloudstarter.minio.MinioConfiguration
import com.jmsoftware.maf.springcloudstarter.poi.ExcelImportConfigurationProperties
import com.jmsoftware.maf.springcloudstarter.property.FeignClientConfigurationProperties
import com.jmsoftware.maf.springcloudstarter.property.JwtConfigurationProperties
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import com.jmsoftware.maf.springcloudstarter.quartz.QuartzConfiguration
import com.jmsoftware.maf.springcloudstarter.rabbitmq.RabbitmqConfiguration
import com.jmsoftware.maf.springcloudstarter.redis.RedisConfiguration
import com.jmsoftware.maf.springcloudstarter.service.CommonService
import com.jmsoftware.maf.springcloudstarter.service.impl.CommonServiceImpl
import com.jmsoftware.maf.springcloudstarter.websocket.WebSocketConfiguration
import org.apache.ibatis.exceptions.PersistenceException
import org.mybatis.spring.MyBatisSystemException
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.condition.*
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.sleuth.Tracer
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.springframework.integration.annotation.IntegrationComponentScan
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import javax.annotation.PostConstruct

/**
 * # MafAutoConfiguration
 *
 * Description: MafAutoConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 9:26 PM
 * @see <a href='https://docs.spring.io/spring-boot/docs/current/reference/html/features.html.features.developing-auto-configuration'>9. Creating Your Own Auto-configuration</a>
 */
@AutoConfiguration
@IntegrationComponentScan
@ConditionalOnWebApplication
@EnableConfigurationProperties(
    MafConfigurationProperties::class,
    MafProjectProperties::class,
    JwtConfigurationProperties::class,
    ExcelImportConfigurationProperties::class,
    FeignClientConfigurationProperties::class
)
@Import(
    WebMvcConfiguration::class,
    MyBatisPlusConfiguration::class,
    RedisConfiguration::class,
    ElasticsearchConfiguration::class,
    WebSecurityConfiguration::class,
    RestTemplateConfiguration::class,
    AsyncConfiguration::class,
    SchedulingConfiguration::class,
    RabbitmqConfiguration::class,
    MinioConfiguration::class,
    JacksonConfiguration::class,
    QuartzConfiguration::class,
    WebSocketConfiguration::class,
    OpenApiConfiguration::class,
    OpenFeignConfiguration::class
)
@ImportAutoConfiguration(
    IntegrationAutoConfiguration::class
)
class MafAutoConfiguration {
    companion object {
        private val log = logger()
    }

    @PostConstruct
    fun postConstruct() {
        log.warn("Post construction of `${this.javaClass.simpleName}`")
    }

    @Bean
    @ConditionalOnMissingBean
    fun trackableResponseControllerAdvice(tracer: Tracer): TrackableResponseControllerAdvice {
        log.warn("Initial bean: `${TrackableResponseControllerAdvice::class.java.simpleName}`")
        return TrackableResponseControllerAdvice(tracer)
    }

    @Bean
    @ConditionalOnMissingBean
    fun commonExceptionControllerAdvice(): CommonExceptionControllerAdvice {
        log.warn("Initial bean: `${CommonExceptionControllerAdvice::class.java.simpleName}`")
        return CommonExceptionControllerAdvice()
    }

    @Bean
    @ConditionalOnClass(
        MyBatisSystemException::class, MybatisPlusException::class, PersistenceException::class
    )
    fun databaseExceptionControllerAdvice(): DatabaseExceptionControllerAdvice {
        log.warn("Initial bean: `${DatabaseExceptionControllerAdvice::class.java.simpleName}`")
        return DatabaseExceptionControllerAdvice()
    }

    @Bean
    @ConditionalOnProperty(value = ["maf.configuration.web-request-log-enabled"])
    fun webRequestLogAspect(objectMapper: ObjectMapper): WebRequestLogAspect {
        log.warn("Initial bean: `${WebRequestLogAspect::class.java.simpleName}`")
        return WebRequestLogAspect(objectMapper)
    }

    @Bean
    fun redirectController(): RedirectController {
        log.warn("Initial bean: `${RedirectController::class.java.simpleName}`")
        return RedirectController()
    }

    @Bean
    fun requestFilter(mafConfigurationProperties: MafConfigurationProperties): AccessLogFilter {
        log.warn("Initial bean: `${AccessLogFilter::class.java.simpleName}`")
        return AccessLogFilter(mafConfigurationProperties)
    }

    @Bean
    fun ipHelper(environment: Environment): IpHelper {
        log.warn("Initial bean: `${IpHelper::class.java.simpleName}`")
        return IpHelper(environment)
    }

    @Bean
    fun springBootStartupHelper(
        mafProjectProperties: MafProjectProperties,
        ipHelper: IpHelper,
        applicationContext: ApplicationContext
    ): SpringBootStartupHelper {
        log.warn("Initial bean: `${SpringBootStartupHelper::class.java.simpleName}`")
        return SpringBootStartupHelper(mafProjectProperties, ipHelper, applicationContext)
    }

    @Bean
    @ConditionalOnMissingBean(value = [ErrorController::class], search = SearchStrategy.CURRENT)
    fun globalErrorController(
        errorAttributes: ErrorAttributes,
        serverProperties: ServerProperties,
        errorViewResolvers: List<ErrorViewResolver>
    ): GlobalErrorController {
        log.warn("Initial bean: `${GlobalErrorController::class.java.simpleName}`")
        return GlobalErrorController(errorAttributes, serverProperties, errorViewResolvers)
    }

    @Bean
    fun httpApiScanHelper(requestMappingHandlerMapping: RequestMappingHandlerMapping): HttpApiScanHelper {
        log.warn("Initial bean: `${HttpApiScanHelper::class.java.simpleName}`")
        return HttpApiScanHelper(requestMappingHandlerMapping)
    }

    @Bean
    fun httpApiResourceRemoteController(
        mafConfigurationProperties: MafConfigurationProperties,
        httpApiScanHelper: HttpApiScanHelper
    ): HttpApiResourceRemoteApiController {
        log.warn("Initial bean: `${HttpApiResourceRemoteApiController::class.java.simpleName}`")
        return HttpApiResourceRemoteApiController(mafConfigurationProperties, httpApiScanHelper)
    }

    @Bean
    @RefreshScope
    fun commonService(mafProjectProperties: MafProjectProperties): CommonService {
        log.warn("Initial bean: `${CommonService::class.java.simpleName}`")
        return CommonServiceImpl(mafProjectProperties)
    }

    @Bean
    fun commonController(commonService: CommonService): CommonController {
        log.warn("Initial bean: `${CommonController::class.java.simpleName}`")
        return CommonController(commonService)
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "feign.client.config.default",
        name = ["enabledAopLog"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun feignClientLogAspect(): FeignClientLogAspect {
        log.warn("Initial bean: `${FeignClientLogAspect::class.java.simpleName}`")
        return FeignClientLogAspect()
    }
}
