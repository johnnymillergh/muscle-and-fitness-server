package com.jmsoftware.maf.reactivespringcloudstarter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.reactivespringcloudstarter.configuration.*;
import com.jmsoftware.maf.reactivespringcloudstarter.controller.CommonController;
import com.jmsoftware.maf.reactivespringcloudstarter.filter.AccessLogFilter;
import com.jmsoftware.maf.reactivespringcloudstarter.helper.IpHelper;
import com.jmsoftware.maf.reactivespringcloudstarter.helper.SpringBootStartupHelper;
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
        MafConfiguration.class,
        MafProjectProperty.class,
        JwtConfiguration.class
})
@Import({
        RedisConfiguration.class,
        WebFluxConfiguration.class,
        WebClientConfiguration.class,
        JacksonConfiguration.class
})
public class MafReactiveAutoConfiguration {
    @PostConstruct
    public void postConstruct() {
        log.warn("Post construction of [{}] is done. About to inject beans. Auto configure order: {}",
                 this.getClass().getSimpleName(), Integer.MIN_VALUE);
    }

    @Bean
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
                                                           IpHelper ipHelper, ApplicationContext applicationContext) {
        log.warn("Initial bean: '{}'", SpringBootStartupHelper.class.getSimpleName());
        return new SpringBootStartupHelper(mafProjectProperty, ipHelper, applicationContext);
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
    public ResponseUtil responseUtil(ObjectMapper objectMapper) {
        log.warn("Initial bean: '{}'", ResponseUtil.class.getSimpleName());
        return new ResponseUtil(objectMapper);
    }
}
