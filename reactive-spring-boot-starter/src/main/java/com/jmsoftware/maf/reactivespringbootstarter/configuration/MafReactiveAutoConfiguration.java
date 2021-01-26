package com.jmsoftware.maf.reactivespringbootstarter.configuration;

import com.jmsoftware.maf.reactivespringbootstarter.controller.CommonController;
import com.jmsoftware.maf.reactivespringbootstarter.filter.AccessLogFilter;
import com.jmsoftware.maf.reactivespringbootstarter.helper.IpHelper;
import com.jmsoftware.maf.reactivespringbootstarter.service.CommonService;
import com.jmsoftware.maf.reactivespringbootstarter.service.impl.CommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Description: MuscleAndFitnessServerReactiveAutoConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/22/2020 2:41 PM
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
@AutoConfigureOrder(Integer.MIN_VALUE)
@EnableConfigurationProperties(MafConfiguration.class)
public class MafReactiveAutoConfiguration {
    @PostConstruct
    public void postConstruct() {
        log.warn("Post construction of [{}] is done. About to inject beans. Auto configure order: {}",
                 getClass().getSimpleName(), Integer.MIN_VALUE);
    }

    @Bean
    public WebFluxConfiguration webFluxConfiguration() {
        log.warn("Initial bean: '{}'", WebFluxConfiguration.class.getSimpleName());
        return new WebFluxConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean
    public MafConfiguration mafConfiguration() {
        log.warn("Initial bean: '{}'", MafConfiguration.class.getSimpleName());
        return new MafConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessLogFilter requestFilter(MafConfiguration mafConfiguration) {
        log.warn("Initial bean: '{}'", AccessLogFilter.class.getSimpleName());
        return new AccessLogFilter(mafConfiguration);
    }

    @Bean
    public MafProjectProperty mafProjectProperty() {
        log.warn("Initial bean: '{}'", MafProjectProperty.class.getSimpleName());
        return new MafProjectProperty();
    }

    @Bean
    public IpHelper ipHelper(MafProjectProperty mafProjectProperty) {
        log.warn("Initial bean: '{}'", IpHelper.class.getSimpleName());
        return new IpHelper(mafProjectProperty);
    }

    @Bean
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
