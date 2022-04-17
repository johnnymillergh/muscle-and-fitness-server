package com.jmsoftware.maf.springcloudstarter.minio

import com.jmsoftware.maf.common.util.logger
import io.minio.MinioClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * # MinioConfiguration
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 3:25 PM
 */
@ConditionalOnProperty("minio.enabled")
@ConditionalOnClass(MinioClient::class)
@EnableConfigurationProperties(MinioProperties::class)
class MinioConfiguration {
    companion object {
        private val log = logger()
    }

    @Bean
    fun minioClient(minioProperties: MinioProperties): MinioClient {
        log.warn("Initial bean: `${MinioClient::class.java.simpleName}`" )
        return MinioClient.builder()
            .endpoint(
                minioProperties.endpoint, minioProperties.port,
                minioProperties.secure
            )
            .credentials(minioProperties.accessKey, minioProperties.secretKey)
            .build()
    }

    @Bean
    fun minioHelper(minioClient: MinioClient): MinioHelper {
        log.warn("Initial bean: `${MinioHelper::class.java.simpleName}`" )
        return MinioHelper(minioClient)
    }
}
