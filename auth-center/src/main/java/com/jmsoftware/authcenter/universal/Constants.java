package com.jmsoftware.authcenter.universal;

import com.jmsoftware.authcenter.universal.configuration.ProjectProperty;
import org.springframework.stereotype.Component;

/**
 * <h1>Constants</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 3/12/20 3:19 PM
 **/
@Component
public class Constants {
    public Constants(ProjectProperty projectProperty) {
        Constants.REDIS_JWT_KEY_PREFIX = projectProperty.getProjectArtifactId() + ":jwt:";
    }

    /**
     * Key prefix of JWT stored in Redis.
     */
    public static String REDIS_JWT_KEY_PREFIX;
    /**
     * Token key of request header.
     */
    public static final String REQUEST_TOKEN_KEY = "Authorization";
    /**
     * Prefix of JWT.
     */
    public static final String JWT_PREFIX = "Bearer ";
}
