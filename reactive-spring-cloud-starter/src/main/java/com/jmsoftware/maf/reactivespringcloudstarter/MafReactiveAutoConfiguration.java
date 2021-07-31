package com.jmsoftware.maf.reactivespringcloudstarter;

import com.jmsoftware.maf.reactivespringcloudstarter.configuration.MafConfiguration;
import com.jmsoftware.maf.reactivespringcloudstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.reactivespringcloudstarter.configuration.WebFluxConfiguration;
import com.jmsoftware.maf.reactivespringcloudstarter.controller.CommonController;
import com.jmsoftware.maf.reactivespringcloudstarter.filter.AccessLogFilter;
import com.jmsoftware.maf.reactivespringcloudstarter.helper.IpHelper;
import com.jmsoftware.maf.reactivespringcloudstarter.helper.SpringBootStartupHelper;
import com.jmsoftware.maf.reactivespringcloudstarter.redis.RedisConfiguration;
import com.jmsoftware.maf.reactivespringcloudstarter.service.CommonService;
import com.jmsoftware.maf.reactivespringcloudstarter.service.impl.CommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

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
@EnableConfigurationProperties({
        MafConfiguration.class,
        MafProjectProperty.class
})
@Import({
        RedisConfiguration.class,
        WebFluxConfiguration.class
})
public class MafReactiveAutoConfiguration {
    @PostConstruct
    public void postConstruct() {
        log.warn("Post construction of [{}] is done. About to inject beans. Auto configure order: {}",
                 this.getClass().getSimpleName(), Integer.MIN_VALUE);
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessLogFilter requestFilter(MafConfiguration mafConfiguration) {
        log.warn("Initial bean: '{}'", AccessLogFilter.class.getSimpleName());
        return new AccessLogFilter(mafConfiguration);
    }

    @Bean
    public IpHelper ipHelper(MafProjectProperty mafProjectProperty) {
        log.warn("Initial bean: '{}'", IpHelper.class.getSimpleName());
        return new IpHelper(mafProjectProperty);
    }

    @Bean
    public SpringBootStartupHelper springBootStartupHelper(MafProjectProperty mafProjectProperty,
                                                           IpHelper ipHelper) {
        log.warn("Initial bean: '{}'", SpringBootStartupHelper.class.getSimpleName());
        return new SpringBootStartupHelper(mafProjectProperty, ipHelper);
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

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        log.warn("Initial bean: '{}'", WebClient.Builder.class.getSimpleName());
        return WebClient.builder();
    }
}
