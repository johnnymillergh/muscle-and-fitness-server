package com.jmsoftware.maf.authcenter.read.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.authcenter.read.entity.GetSingleResourcePayload;
import com.jmsoftware.maf.authcenter.read.service.ReadResourceService;
import com.jmsoftware.maf.springcloudstarter.helper.MinioHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

import static java.lang.Math.min;

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

    @Override
    public ResponseEntity<ResourceRegion> getResourceRegion(String header, String bucket, String object) {
        val statObjectResponse = minioHelper.statObject("bucket", "object");
        if (ObjectUtil.isNull(statObjectResponse)) {
            return ResponseEntity.notFound().build();
        }
        val inputStream = minioHelper.getObject("bucket", "object");
        final InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        ResourceRegion resourceRegion = getResourceRegion(inputStreamResource, object);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(inputStreamResource)
                                     .orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resourceRegion);
    }

    @SneakyThrows({IOException.class})
    private ResourceRegion getResourceRegion(Resource resource, String httpHeaders) {
        ResourceRegion resourceRegion;
        long contentLength = resource.contentLength();
        int fromRange = 0;
        int toRange = 0;
        if (StrUtil.isNotBlank(httpHeaders)) {
            String[] ranges = httpHeaders.substring("bytes=".length()).split("-");
            fromRange = Integer.parseInt(ranges[0]);
            if (ranges.length > 1) {
                toRange = Integer.parseInt(ranges[1]);
            } else {
                toRange = (int) (contentLength - 1);
            }
        }
        if (fromRange > 0) {
            long rangeLength = min(CHUNK_SIZE, toRange - fromRange + 1);
            resourceRegion = new ResourceRegion(resource, fromRange, rangeLength);
        } else {
            long rangeLength = min(CHUNK_SIZE, contentLength);
            resourceRegion = new ResourceRegion(resource, 0, rangeLength);
        }
        return resourceRegion;
    }
}
