package com.jmsoftware.maf.springcloudstarter;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.jmsoftware.maf.springcloudstarter.aspect.CommonExceptionControllerAdvice;
import com.jmsoftware.maf.springcloudstarter.aspect.DatabaseExceptionControllerAdvice;
import com.jmsoftware.maf.springcloudstarter.aspect.WebRequestLogAspect;
import com.jmsoftware.maf.springcloudstarter.configuration.*;
import com.jmsoftware.maf.springcloudstarter.controller.CommonController;
import com.jmsoftware.maf.springcloudstarter.controller.GlobalErrorController;
import com.jmsoftware.maf.springcloudstarter.controller.HttpApiResourceRemoteApiController;
import com.jmsoftware.maf.springcloudstarter.controller.RedirectController;
import com.jmsoftware.maf.springcloudstarter.database.MyBatisPlusConfiguration;
import com.jmsoftware.maf.springcloudstarter.filter.AccessLogFilter;
import com.jmsoftware.maf.springcloudstarter.helper.HttpApiScanHelper;
import com.jmsoftware.maf.springcloudstarter.helper.IpHelper;
import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper;
import com.jmsoftware.maf.springcloudstarter.minio.MinioConfiguration;
import com.jmsoftware.maf.springcloudstarter.poi.ExcelImportConfiguration;
import com.jmsoftware.maf.springcloudstarter.quartz.QuartzConfiguration;
import com.jmsoftware.maf.springcloudstarter.redis.RedisConfiguration;
import com.jmsoftware.maf.springcloudstarter.service.CommonService;
import com.jmsoftware.maf.springcloudstarter.service.impl.CommonServiceImpl;
import com.jmsoftware.maf.springcloudstarter.sftp.SftpConfiguration;
import com.jmsoftware.maf.springcloudstarter.websocket.WebSocketConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Description: MediaStreamingAutoConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 10/19/2020 2:51 PM
 **/
@Slf4j
@IntegrationComponentScan
@ConditionalOnWebApplication
@EnableConfigurationProperties({
        MafConfiguration.class,
        MafProjectProperty.class,
        JwtConfiguration.class,
        ExcelImportConfiguration.class
})
@Import({
        WebMvcConfiguration.class,
        MyBatisPlusConfiguration.class,
        RedisConfiguration.class,
        Swagger2Configuration.class,
        SftpConfiguration.class,
        WebSecurityConfiguration.class,
        RestTemplateConfiguration.class,
        AsyncConfiguration.class,
        RabbitmqConfiguration.class,
        MinioConfiguration.class,
        JacksonConfiguration.class,
        TypeConversionConfiguration.class,
        QuartzConfiguration.class,
        WebSocketConfiguration.class
})
public class MafAutoConfiguration {
    @PostConstruct
    public void postConstruct() {
        log.warn("Post construction of '{}'", this.getClass().getSimpleName());
    }

    @Bean
    @ConditionalOnMissingBean
    public CommonExceptionControllerAdvice exceptionControllerAdvice() {
        log.warn("Initial bean: '{}'", CommonExceptionControllerAdvice.class.getSimpleName());
        return new CommonExceptionControllerAdvice();
    }

    @Bean
    @ConditionalOnClass({MyBatisSystemException.class, MybatisPlusException.class, PersistenceException.class})
    public DatabaseExceptionControllerAdvice databaseExceptionControllerAdvice() {
        log.warn("Initial bean: '{}'", DatabaseExceptionControllerAdvice.class.getSimpleName());
        return new DatabaseExceptionControllerAdvice();
    }

    @Bean
    @ConditionalOnProperty(value = "maf.configuration.web-request-log-enabled")
    public WebRequestLogAspect webRequestLogAspect() {
        log.warn("Initial bean: '{}'", WebRequestLogAspect.class.getSimpleName());
        return new WebRequestLogAspect();
    }

    @Bean
    public RedirectController redirectController() {
        log.warn("Initial bean: '{}'", RedirectController.class.getSimpleName());
        return new RedirectController();
    }

    @Bean
    public AccessLogFilter requestFilter(MafConfiguration mafConfiguration) {
        log.warn("Initial bean: '{}'", AccessLogFilter.class.getSimpleName());
        return new AccessLogFilter(mafConfiguration);
    }

    @Bean
    public IpHelper ipHelper(Environment environment) {
        log.warn("Initial bean: '{}'", IpHelper.class.getSimpleName());
        return new IpHelper(environment);
    }

    @Bean
    public SpringBootStartupHelper springBootStartupHelper(MafProjectProperty mafProjectProperty,
                                                           IpHelper ipHelper, ApplicationContext applicationContext) {
        log.warn("Initial bean: '{}'", SpringBootStartupHelper.class.getSimpleName());
        return new SpringBootStartupHelper(mafProjectProperty, ipHelper, applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorController.class, search = SearchStrategy.CURRENT)
    public GlobalErrorController globalErrorController(ErrorAttributes errorAttributes,
                                                       ServerProperties serverProperties,
                                                       List<ErrorViewResolver> errorViewResolvers) {
        log.warn("Initial bean: '{}'", GlobalErrorController.class.getSimpleName());
        return new GlobalErrorController(errorAttributes, serverProperties, errorViewResolvers);
    }

    @Bean
    public HttpApiScanHelper httpApiScanHelper(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        log.warn("Initial bean: '{}'", HttpApiScanHelper.class.getSimpleName());
        return new HttpApiScanHelper(requestMappingHandlerMapping);
    }

    @Bean
    public HttpApiResourceRemoteApiController httpApiResourceRemoteController(MafConfiguration mafConfiguration,
                                                                              HttpApiScanHelper httpApiScanHelper) {
        log.warn("Initial bean: '{}'", HttpApiResourceRemoteApiController.class.getSimpleName());
        return new HttpApiResourceRemoteApiController(mafConfiguration, httpApiScanHelper);
    }

    @Bean
    @RefreshScope
    public CommonService commonService(MafProjectProperty mafProjectProperty) {
        log.warn("Initial bean: '{}'", CommonServiceImpl.class.getSimpleName());
        return new CommonServiceImpl(mafProjectProperty);
    }

    @Bean
    public CommonController commonController(CommonService commonService) {
        log.warn("Initial bean: '{}'", CommonController.class.getSimpleName());
        return new CommonController(commonService);
    }
}
