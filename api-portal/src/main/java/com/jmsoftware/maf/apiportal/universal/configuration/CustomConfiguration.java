package com.jmsoftware.maf.apiportal.universal.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
     * <p>The username of super user who has no restriction to access any system&#39;s resources.</p>
     * <p><strong>ATTENTION</strong>: The value of username of super user must be equal to the value that is
     * persistent in database.</p>
     */
    private String superUser;
    /**
     * Ignore URLs
     */
    private IgnoredRequest ignoredRequest;
    private String druidLoginName;
    private String druidPassword;
    /**
     * <p>Web security feature switch. Default is false.</p>
     * true - disable web security; false - enable web security.
     */
    private Boolean webSecurityDisabled = false;
    /**
     * Web request log switch. Default is false.
     * <p>
     * true - disable web request log; false - enable web request log.
     */
    private Boolean webRequestLogDisabled = false;
}
