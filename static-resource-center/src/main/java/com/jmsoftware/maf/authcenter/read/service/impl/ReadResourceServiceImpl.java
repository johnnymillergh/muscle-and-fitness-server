package com.jmsoftware.maf.authcenter.read.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jmsoftware.maf.authcenter.read.entity.GetSingleResourcePayload;
import com.jmsoftware.maf.authcenter.read.service.ReadResourceService;
import com.jmsoftware.maf.springcloudstarter.helper.MinioHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <h1>ReadResourceServiceImpl</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 5:18 PM
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ReadResourceServiceImpl implements ReadResourceService {
    private final MinioHelper minioHelper;

    @Override
    public ResponseEntity<Resource> getSingleResource(@NotBlank String bucket, @NotBlank String object,
                                                      @Valid @NotNull GetSingleResourcePayload payload) {
        val statObjectResponse = minioHelper.statObject(bucket, object);
        if (ObjectUtil.isNull(statObjectResponse)) {
            return ResponseEntity.notFound().build();
        }
        val inputStream = minioHelper.getObject(bucket, object);
        val bodyBuilder = ResponseEntity.ok();
        if (BooleanUtil.isTrue(payload.getDownloadable())) {
            String contentDisposition = ContentDisposition.builder("attachment").filename(object).build().toString();
            bodyBuilder.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        }
        return bodyBuilder
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .contentLength(statObjectResponse.size())
                .contentType(MediaType.parseMediaType(statObjectResponse.contentType()))
                .body(new InputStreamResource(inputStream));
    }
}
