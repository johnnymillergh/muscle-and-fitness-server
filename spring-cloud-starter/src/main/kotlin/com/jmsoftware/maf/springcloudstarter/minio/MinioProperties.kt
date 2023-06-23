package com.jmsoftware.maf.springcloudstarter.minio

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

/**
 * # MinioProperty
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 3:43 PM
 */
@Validated
@Configuration
@ConfigurationProperties(prefix = MinioProperties.PREFIX)
class MinioProperties {
    companion object {
        /**
         * The constant PREFIX.
         */
        const val PREFIX = "minio"
    }

    /**
     * The Enabled.
     */
    var enabled = false

    /**
     * The Endpoint.
     */
    lateinit var endpoint: @NotBlank String

    /**
     * The Port.
     */
    var port: @NotNull Int = 0

    /**
     * The Access key. User ID
     */
    lateinit var accessKey: @NotBlank String

    /**
     * The Secret key. Password
     */
    lateinit var secretKey: @NotBlank String

    /**
     * The Secure.
     */
    var secure = false

    /**
     * The Bucket name.
     */
    var bucketName: String? = null

    /**
     * The Config dir.
     */
    var configDir: String? = null
}
