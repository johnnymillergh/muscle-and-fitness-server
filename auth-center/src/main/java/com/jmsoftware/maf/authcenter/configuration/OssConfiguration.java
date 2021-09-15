package com.jmsoftware.maf.authcenter.configuration;

import com.jmsoftware.maf.authcenter.remoteapi.osscenter.OssCenterRemoteApi;
import com.jmsoftware.maf.springcloudstarter.poi.OssUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description: OssConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/15/2021 11:51 AM
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class OssConfiguration {
    private final OssCenterRemoteApi ossCenterRemoteApi;

    @Bean
    public OssUploader ossUploader() {
        return (name, inputStream) -> {
            MultipartFile file = new MockMultipartFile(name, name, null, inputStream);
            val response = this.ossCenterRemoteApi.uploadSingleResource(file);
            log.info("Called {} to upload file. {}", OssCenterRemoteApi.SERVICE_NAME, response);
            return String.format("%s/%s", response.getData().getBucket(), response.getData().getObject());
        };
    }
}
