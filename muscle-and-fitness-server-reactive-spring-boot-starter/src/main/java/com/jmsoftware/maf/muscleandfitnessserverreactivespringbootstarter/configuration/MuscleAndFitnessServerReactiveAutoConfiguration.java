package com.jmsoftware.maf.muscleandfitnessserverreactivespringbootstarter.configuration;

import com.jmsoftware.maf.muscleandfitnessserverreactivespringbootstarter.filter.AccessLogFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@EnableConfigurationProperties(MuscleAndFitnessServerReactiveAutoConfiguration.class)
public class MuscleAndFitnessServerReactiveAutoConfiguration {
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
    public AccessLogFilter requestFilter(MafConfiguration mafConfiguration) {
        log.debug("Initial bean: {}", AccessLogFilter.class.getName());
        return new AccessLogFilter(mafConfiguration);
    }
}
