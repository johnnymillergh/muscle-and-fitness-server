package com.jmsoftware.maf.springbootstarter.configuration;

import com.jmsoftware.maf.springbootstarter.aspect.ExceptionControllerAdvice;
import com.jmsoftware.maf.springbootstarter.aspect.WebRequestLogAspect;
import com.jmsoftware.maf.springbootstarter.controller.CommonController;
import com.jmsoftware.maf.springbootstarter.controller.GlobalErrorController;
import com.jmsoftware.maf.springbootstarter.controller.HttpApiResourceRemoteApiController;
import com.jmsoftware.maf.springbootstarter.controller.RedirectController;
import com.jmsoftware.maf.springbootstarter.filter.AccessLogFilter;
import com.jmsoftware.maf.springbootstarter.helper.HttpApiScanHelper;
import com.jmsoftware.maf.springbootstarter.helper.IpHelper;
import com.jmsoftware.maf.springbootstarter.service.CommonService;
import com.jmsoftware.maf.springbootstarter.service.impl.CommonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Description: MediaStreamingAutoConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 10/19/2020 2:51 PM
 **/
@Slf4j
@Configuration
@ConditionalOnWebApplication
@AutoConfigureOrder(Integer.MIN_VALUE)
@EnableConfigurationProperties(MafConfiguration.class)
public class MafAutoConfiguration {
    @PostConstruct
    public void postConstruct() {
        log.warn("Post construction of [{}] is done. About to inject beans. Auto configure order: {}",
                 getClass().getSimpleName(), Integer.MIN_VALUE);
    }

    @Bean
    @ConditionalOnMissingBean
    public MafConfiguration mafConfiguration() {
        log.warn("Initial bean: {}", MafConfiguration.class.getSimpleName());
        val mafConfiguration = new MafConfiguration();
        log.warn("{}", mafConfiguration);
        return mafConfiguration;
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionControllerAdvice exceptionControllerAdvice() {
        log.warn("Initial bean: {}", ExceptionControllerAdvice.class.getSimpleName());
        return new ExceptionControllerAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "maf.configuration.web-request-log-disabled", havingValue = "false")
    public WebRequestLogAspect webRequestLogAspect() {
        log.warn("Initial bean: {}", WebRequestLogAspect.class.getSimpleName());
        return new WebRequestLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public RedirectController redirectController() {
        log.warn("Initial bean: {}", RedirectController.class.getSimpleName());
        return new RedirectController();
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessLogFilter requestFilter(MafConfiguration mafConfiguration) {
        log.warn("Initial bean: {}", AccessLogFilter.class.getSimpleName());
        return new AccessLogFilter(mafConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    public IpHelper ipHelper(Environment environment) {
        log.warn("Initial bean: {}", IpHelper.class.getSimpleName());
        return new IpHelper(environment);
    }

    @Bean
    @ConditionalOnMissingBean
    public WebMvcConfiguration webMvcConfiguration() {
        log.warn("Initial bean: {}", WebMvcConfiguration.class.getSimpleName());
        return new WebMvcConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorController.class, search = SearchStrategy.CURRENT)
    public GlobalErrorController globalErrorController(ErrorAttributes errorAttributes,
                                                       ServerProperties serverProperties,
                                                       List<ErrorViewResolver> errorViewResolvers) {
        log.warn("Initial bean: {}", GlobalErrorController.class.getSimpleName());
        return new GlobalErrorController(errorAttributes, serverProperties, errorViewResolvers);
    }

    @Bean
    public HttpApiScanHelper httpApiScanHelper(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        log.warn("Initial bean: {}", HttpApiScanHelper.class.getSimpleName());
        return new HttpApiScanHelper(requestMappingHandlerMapping);
    }

    @Bean
    public HttpApiResourceRemoteApiController httpApiResourceRemoteController(MafConfiguration mafConfiguration,
                                                                              HttpApiScanHelper httpApiScanHelper) {
        log.warn("Initial bean: {}", HttpApiResourceRemoteApiController.class.getSimpleName());
        return new HttpApiResourceRemoteApiController(mafConfiguration, httpApiScanHelper);
    }

    @Bean
    public MafProjectProperty mafProjectProperty() {
        log.warn("Initial bean: {}", MafProjectProperty.class.getSimpleName());
        return new MafProjectProperty();
    }

    @Bean
    @ConditionalOnProperty(value ="maf.configuration.swagger-disabled", havingValue = "false")
    public Swagger2Configuration swagger2Configuration(MafProjectProperty mafProjectProperty) {
        log.warn("Initial bean: {}", Swagger2Configuration.class.getSimpleName());
        return new Swagger2Configuration(mafProjectProperty);
    }

    @Bean
    @ConditionalOnProperty(value ="maf.configuration.swagger-disabled", havingValue = "false")
    public Docket docket(Swagger2Configuration swagger2Configuration, MafProjectProperty mafProjectProperty) {
        log.warn("Initial bean: {}", Docket.class.getSimpleName());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swagger2Configuration.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(mafProjectProperty.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public CommonService commonService(MafProjectProperty mafProjectProperty) {
        log.warn("Initial bean: {}", CommonServiceImpl.class.getSimpleName());
        return new CommonServiceImpl(mafProjectProperty);
    }

    @Bean
    public CommonController commonController(CommonService commonService) {
        log.warn("Initial bean: {}", CommonController.class.getSimpleName());
        return new CommonController(commonService);
    }
}
