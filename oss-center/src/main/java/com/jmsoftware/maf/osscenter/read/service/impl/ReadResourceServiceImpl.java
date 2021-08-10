package com.jmsoftware.maf.osscenter.read.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import com.jmsoftware.maf.osscenter.read.entity.GetSingleResourcePayload;
import com.jmsoftware.maf.osscenter.read.service.ReadResourceService;
import com.jmsoftware.maf.springcloudstarter.helper.MinioHelper;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private static final DataSize SMALL_DATA_SIZE = DataSize.ofMegabytes(1);
    private static final DataSize MEDIUM_DATA_SIZE = DataSize.ofMegabytes(4);
    private static final DataSize LARGE_DATA_SIZE = DataSize.ofMegabytes(8);
    private final MinioHelper minioHelper;

    @Override
    public ResponseEntity<Resource> getSingleResource(@NotBlank String bucket, @NotBlank String object,
                                                      @Valid @NotNull GetSingleResourcePayload payload,
                                                      @Nullable String range) {
        StatObjectResponse statObjectResponse;
        try {
            statObjectResponse = this.minioHelper.statObject(bucket, object);
        } catch (Exception e) {
            log.error("Exception occurred when looking for object. Exception message: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
        val httpRanges = HttpRange.parseRanges(range);
        if (CollUtil.isEmpty(httpRanges)) {
            val bodyBuilder = ResponseEntity.ok();
            if (BooleanUtil.isTrue(payload.getDownloadable())) {
                bodyBuilder.header(HttpHeaders.CONTENT_DISPOSITION,
                                   ContentDisposition.builder("attachment").filename(object).build().toString());
            }
            val getObjectResponse = this.minioHelper.getObject(bucket, object, 0, MEDIUM_DATA_SIZE.toBytes());
            return bodyBuilder
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .contentLength(statObjectResponse.size())
                    .contentType(MediaType.parseMediaType(statObjectResponse.contentType()))
                    .body(new InputStreamResource(getObjectResponse));
        }
        return this.getResourceRegion(bucket, object, statObjectResponse, httpRanges);
    }

    private ResponseEntity<Resource> getResourceRegion(String bucket, String object,
                                                       StatObjectResponse statObjectResponse,
                                                       List<HttpRange> httpRanges) {
        val bodyBuilder = ResponseEntity.status(HttpStatus.PARTIAL_CONTENT);
        val getObjectResponse = this.minioHelper.getObject(bucket, object, httpRanges.get(0).getRangeStart(0),
                                                           MEDIUM_DATA_SIZE.toBytes());
        val start = httpRanges.get(0).getRangeStart(0);
        var end = start + MEDIUM_DATA_SIZE.toBytes() - 1;
        val resourceLength = statObjectResponse.size();
        end = Math.min(end, resourceLength - 1);
        val rangeLength = end - start + 1;
        return bodyBuilder
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_RANGE, String.format("bytes %d-%d/%d", start, end, resourceLength))
                .contentLength(rangeLength)
                .contentType(MediaType.parseMediaType(statObjectResponse.contentType()))
                .body(new InputStreamResource(getObjectResponse));
    }
}
