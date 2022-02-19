package com.jmsoftware.maf.authcenter.configuration;

import com.jmsoftware.maf.authcenter.remote.OssCenterFeignService;
import com.jmsoftware.maf.springcloudstarter.poi.OssUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;

import static cn.hutool.core.text.CharSequenceUtil.format;

/**
 * Description: OssConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/15/2021 11:51 AM
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class OssConfiguration {
    private final OssCenterFeignService ossCenterFeignService;

    @Bean
    public OssUploader ossUploader() {
        return (name, inputStream) -> {
            val multipartFile = new MockMultipartFile(name, name, null, inputStream);
            val objectResponse = this.ossCenterFeignService.uploadSingleResource(multipartFile);
            log.info("Uploaded multipartFile. objectResponse: {}", objectResponse);
            return format("{}/{}", objectResponse.getBucket(), objectResponse.getObject());
        };
    }
}
