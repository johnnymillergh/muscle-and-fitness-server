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
