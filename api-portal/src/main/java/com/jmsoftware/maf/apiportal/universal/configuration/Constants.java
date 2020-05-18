package com.jmsoftware.maf.apiportal.universal.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <h1>Constants</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/2/20 11:41 PM
 **/
@Slf4j
@Component
public class Constants {
    public Constants(ProjectProperty projectProperty) {
        REDIS_JWT_KEY_PREFIX = String.format("%s:jwt:", projectProperty.getParentArtifactId());
        log.info("Initiated 'REDIS_JWT_KEY_PREFIX': {}", REDIS_JWT_KEY_PREFIX);
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
    /**
     * Star sign
     */
    public static final String ASTERISK = "*";
    /**
     * At sign
     */
    public static final String AT_SIGN = "@";
}
