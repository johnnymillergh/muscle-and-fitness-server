package com.jmsoftware.gateway.universal.configuration;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <h1>CustomConfiguration</h1>
 * <p>Custom configurations which are written in .yml files, containing a variety of fragmentary configs. Such as,
 * Druid login info, web security switch, web log and so on.</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 14:24
 **/
@Data
@Component
@ConfigurationProperties(prefix = "custom.configuration")
public class CustomConfiguration {
    /**
     * The Allowed application list. If it's empty, gateway will allow all request to any applications (microservices)
     */
    private List<String> allowedApplicationList = Lists.newLinkedList();
}
