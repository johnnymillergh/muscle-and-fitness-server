package com.jmsoftware.maf.springcloudstarter.configuration;

import com.jmsoftware.maf.springcloudstarter.helper.MinioHelper;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * <h1>MinioConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/7/21 9:58 PM
 **/
@Slf4j
@Import({
        MinioProperty.class
})
@ConditionalOnProperty({"minio.enabled"})
public class MinioConfiguration {
    @Bean
    public MinioClient minioClient(MinioProperty minioProperty) {
        log.warn("Initial bean: '{}'", MinioClient.class.getSimpleName());
        return MinioClient.builder()
                .endpoint(minioProperty.getEndpoint(), minioProperty.getPort(),
                          minioProperty.getSecure())
                .credentials(minioProperty.getAccessKey(), minioProperty.getSecretKey())
                .build();
    }

    @Bean
    public MinioHelper minioHelper(MinioClient minioClient) {
        log.warn("Initial bean: '{}'", MinioHelper.class.getSimpleName());
        return new MinioHelper(minioClient);
    }
}
