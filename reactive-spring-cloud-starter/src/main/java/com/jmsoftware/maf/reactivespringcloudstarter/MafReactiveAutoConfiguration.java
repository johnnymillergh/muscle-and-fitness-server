package com.jmsoftware.maf.reactivespringcloudstarter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.reactivespringcloudstarter.configuration.WebClientConfiguration;
import com.jmsoftware.maf.reactivespringcloudstarter.configuration.WebFluxConfiguration;
import com.jmsoftware.maf.reactivespringcloudstarter.controller.CommonController;
import com.jmsoftware.maf.reactivespringcloudstarter.filter.AccessLogFilter;
import com.jmsoftware.maf.reactivespringcloudstarter.helper.IpHelper;
import com.jmsoftware.maf.reactivespringcloudstarter.helper.SpringBootStartupHelper;
import com.jmsoftware.maf.reactivespringcloudstarter.property.JwtConfigurationProperties;
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafConfigurationProperties;
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafProjectProperties;
import com.jmsoftware.maf.reactivespringcloudstarter.redis.RedisConfiguration;
import com.jmsoftware.maf.reactivespringcloudstarter.service.CommonService;
import com.jmsoftware.maf.reactivespringcloudstarter.service.impl.CommonServiceImpl;
import com.jmsoftware.maf.reactivespringcloudstarter.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * Description: MuscleAndFitnessServerReactiveAutoConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/22/2020 2:41 PM
 **/
@Slf4j
@AutoConfigureOrder(Integer.MIN_VALUE)
@EnableConfigurationProperties({
        MafConfigurationProperties.class,
        MafProjectProperties.class,
        JwtConfigurationProperties.class
})
@Import({
        RedisConfiguration.class,
        WebFluxConfiguration.class,
        WebClientConfiguration.class
})
public class MafReactiveAutoConfiguration {
    @PostConstruct
    public void postConstruct() {
        log.warn("Post construction of [{}] is done. About to inject beans. Auto configure order: {}",
                 this.getClass().getSimpleName(), Integer.MIN_VALUE);
    }

    @Bean
    public AccessLogFilter requestFilter(MafConfigurationProperties mafConfigurationProperties) {
        log.warn("Initial bean: '{}'", AccessLogFilter.class.getSimpleName());
        return new AccessLogFilter(mafConfigurationProperties);
    }

    @Bean
    public IpHelper ipHelper(MafProjectProperties mafProjectProperties) {
        log.warn("Initial bean: '{}'", IpHelper.class.getSimpleName());
        return new IpHelper(mafProjectProperties);
    }

    @Bean
    public SpringBootStartupHelper springBootStartupHelper(MafProjectProperties mafProjectProperties,
                                                           IpHelper ipHelper, ApplicationContext applicationContext) {
        log.warn("Initial bean: '{}'", SpringBootStartupHelper.class.getSimpleName());
        return new SpringBootStartupHelper(mafProjectProperties, ipHelper, applicationContext);
    }

    @Bean
    public CommonService commonService(MafProjectProperties mafProjectProperties) {
        log.warn("Initial bean: '{}'", CommonServiceImpl.class.getSimpleName());
        return new CommonServiceImpl(mafProjectProperties);
    }

    @Bean
    public CommonController commonController(CommonService commonService) {
        log.warn("Initial bean: '{}'", CommonController.class.getSimpleName());
        return new CommonController(commonService);
    }

    @Bean
    public ResponseUtil responseUtil(ObjectMapper objectMapper) {
        log.warn("Initial bean: '{}'", ResponseUtil.class.getSimpleName());
        return new ResponseUtil(objectMapper);
    }
}
