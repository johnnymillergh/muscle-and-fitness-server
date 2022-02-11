/*
 * Copyright By ZATI
 * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
 * All rights reserved.
 *
 * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or
 * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your
 * system.
 */
package com.jmsoftware.maf.authcenter.remote.impl;

import com.jmsoftware.maf.authcenter.remote.OssCenterFeignClient;
import com.jmsoftware.maf.authcenter.remote.OssCenterFeignService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Description: OssCenterFeignServiceImpl, change description here.
 *
 * @author 钟俊 (za-zhongjun), email: jun.zhong@zatech.com, date: 2/5/2022 7:47 PM
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class OssCenterFeignServiceImpl implements OssCenterFeignService {
    private final OssCenterFeignClient ossCenterFeignClient;

    @Override
    public ObjectResponse uploadSingleResource(@NotNull MultipartFile multipartFile) {
        log.info("Uploading single resource to oss center. multipartFile: {}", multipartFile);
        return Optional.ofNullable(ossCenterFeignClient.uploadSingleResource(multipartFile))
                .map(ResponseBodyBean::getData)
                .orElseThrow(() -> {
                    log.error("Failed to upload single resource to oss center. multipartFile: {}", multipartFile);
                    return new RuntimeException("Upload single resource to oss center failed.");
                });
    }
}
