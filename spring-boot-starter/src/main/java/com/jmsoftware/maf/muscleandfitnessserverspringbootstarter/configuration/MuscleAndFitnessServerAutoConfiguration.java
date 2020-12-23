package com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.configuration;

import com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.aspect.ExceptionControllerAdvice;
import com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.aspect.WebRequestLogAspect;
import com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.controller.RedirectController;
import com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.filter.AccessLogFilter;
import com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.helper.IpHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * Description: MediaStreamingAutoConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 10/19/2020 2:51 PM
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MuscleAndFitnessServerAutoConfiguration.class)
public class MuscleAndFitnessServerAutoConfiguration {
    @PostConstruct
    public void afterInitialization() {
        log.debug("{} initialization is done. About to inject beans.", getClass().getSimpleName());
    }

    @Bean
    @ConditionalOnMissingBean
    public MafConfiguration mafConfiguration() {
        log.debug("Initial bean: {}", MafConfiguration.class.getName());
        return new MafConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionControllerAdvice exceptionControllerAdvice() {
        log.debug("Initial bean: {}", ExceptionControllerAdvice.class.getName());
        return new ExceptionControllerAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "maf.configuration.webRequestLogDisabled", havingValue = "false")
    public WebRequestLogAspect webRequestLogAspect() {
        log.debug("Initial bean: {}", WebRequestLogAspect.class.getName());
        return new WebRequestLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public RedirectController redirectController() {
        log.debug("Initial bean: {}", RedirectController.class.getName());
        return new RedirectController();
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessLogFilter requestFilter(MafConfiguration mafConfiguration) {
        log.debug("Initial bean: {}", AccessLogFilter.class.getName());
        return new AccessLogFilter(mafConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    public IpHelper ipHelper(Environment environment) {
        log.debug("Initial bean: {}", IpHelper.class.getName());
        return new IpHelper(environment);
    }

    @Bean
    @ConditionalOnMissingBean
    public WebMvcConfiguration webMvcConfiguration() {
        log.debug("Initial bean: {}", WebMvcConfiguration.class.getName());
        return new WebMvcConfiguration();
    }
}
