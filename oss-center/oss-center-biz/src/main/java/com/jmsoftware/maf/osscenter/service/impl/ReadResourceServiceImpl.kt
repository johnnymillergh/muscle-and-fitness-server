package com.jmsoftware.maf.osscenter.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.NioUtil;
import com.jmsoftware.maf.common.exception.ResourceNotFoundException;
import com.jmsoftware.maf.osscenter.response.SerializableStatObjectResponse;
import com.jmsoftware.maf.osscenter.service.ReadResourceService;
import com.jmsoftware.maf.springcloudstarter.minio.MinioHelper;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.jmsoftware.maf.osscenter.constant.Chunk.LARGE_CHUNK_SIZE;
import static com.jmsoftware.maf.osscenter.constant.Chunk.TINY_CHUNK_SIZE;

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
    private static final String ACCEPT_RANGES_VALUE = "bytes";
    private final MinioHelper minioHelper;

    @Override
    @SuppressWarnings("DuplicatedCode")
    public ResponseEntity<StreamingResponseBody> asyncGetSingleResource(
            @NotBlank String bucket,
            @NotBlank String object
    ) {
        StatObjectResponse statObjectResponse;
        try {
            statObjectResponse = this.minioHelper.statObject(bucket, object);
        } catch (Exception e) {
            log.error("Exception occurred when looking for object!", e);
            return ResponseEntity.notFound().build();
        }
        val objectResponse = this.minioHelper.getObject(bucket, object);
        if (statObjectResponse == null || objectResponse == null) {
            log.warn("Object not found! Bucket: {}, Object: {}", bucket, object);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT_RANGES, ACCEPT_RANGES_VALUE)
                .contentLength(statObjectResponse.size())
                .contentType(MediaType.parseMediaType(statObjectResponse.contentType()))
                .body(outputStream -> {
                    NioUtil.copyByNIO(objectResponse, outputStream, NioUtil.DEFAULT_BUFFER_SIZE, null);
                    IoUtil.close(objectResponse);
                });
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public ResponseEntity<StreamingResponseBody> asyncStreamSingleResource(
            @NotBlank String bucket,
            @NotBlank String object,
            @Nullable String range
    ) {
        StatObjectResponse statObjectResponse;
        try {
            statObjectResponse = this.minioHelper.statObject(bucket, object);
        } catch (Exception e) {
            log.error("Exception occurred when looking for object. Exception message: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
        if (statObjectResponse == null) {
            log.warn("StatObjectResponse not found! Bucket: {}, Object: {}", bucket, object);
            return ResponseEntity.notFound().build();
        }
        val httpRanges = HttpRange.parseRanges(range);
        if (CollUtil.isEmpty(httpRanges)) {
            val objectResponse = this.minioHelper.getObject(bucket, object, 0, TINY_CHUNK_SIZE.toBytes());
            if (objectResponse == null) {
                log.warn("Object not found! Bucket: {}, Object: {}", bucket, object);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.ACCEPT_RANGES, ACCEPT_RANGES_VALUE)
                    .contentLength(statObjectResponse.size())
                    .contentType(MediaType.parseMediaType(statObjectResponse.contentType()))
                    .body(outputStream -> {
                        NioUtil.copyByNIO(objectResponse, outputStream, NioUtil.DEFAULT_BUFFER_SIZE, null);
                        IoUtil.close(objectResponse);
                    });
        }
        return this.asyncGetResourceRegion(bucket, object, statObjectResponse, httpRanges);
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public ResponseEntity<StreamingResponseBody> asyncDownloadSingleResource(
            @NotBlank String bucket,
            @NotBlank String object
    ) {
        StatObjectResponse statObjectResponse;
        try {
            statObjectResponse = this.minioHelper.statObject(bucket, object);
        } catch (Exception e) {
            log.error("Exception occurred when looking for object. Exception message: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
        val objectResponse = this.minioHelper.getObject(bucket, object);
        if (statObjectResponse == null || objectResponse == null) {
            log.warn("StatObjectResponse not found! Bucket: {}, Object: {}", bucket, object);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT_RANGES, ACCEPT_RANGES_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(object).build().toString())
                .contentLength(statObjectResponse.size())
                .contentType(MediaType.parseMediaType(statObjectResponse.contentType()))
                .body((outputStream -> {
                    NioUtil.copyByNIO(objectResponse, outputStream, NioUtil.DEFAULT_LARGE_BUFFER_SIZE, null);
                    IoUtil.close(objectResponse);
                }));
    }

    @Override
    @SneakyThrows
    public SerializableStatObjectResponse stateObject(
            @NotBlank String bucket,
            @NotBlank String object
    ) {
        val statObjectResponse = this.minioHelper.statObject(bucket, object);
        if (statObjectResponse == null) {
            log.warn("StatObjectResponse not found! Bucket: {}, Object: {}", bucket, object);
            throw new ResourceNotFoundException("Object not found!");
        }
        return SerializableStatObjectResponse.build(statObjectResponse);
    }

    @SuppressWarnings("DuplicatedCode")
    private ResponseEntity<StreamingResponseBody> asyncGetResourceRegion(
            String bucket,
            String object,
            StatObjectResponse statObjectResponse,
            List<HttpRange> httpRanges
    ) {
        val objectResponse =
                this.minioHelper.getObject(
                        bucket,
                        object,
                        httpRanges.get(0).getRangeStart(0),
                        LARGE_CHUNK_SIZE.toBytes()
                );
        if (objectResponse == null) {
            log.warn("Object not found! Bucket: {}, Object: {}", bucket, object);
            return ResponseEntity.notFound().build();
        }
        val start = httpRanges.get(0).getRangeStart(0);
        var end = start + LARGE_CHUNK_SIZE.toBytes() - 1;
        val resourceLength = statObjectResponse.size();
        end = Math.min(end, resourceLength - 1);
        val rangeLength = end - start + 1;
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.ACCEPT_RANGES, ACCEPT_RANGES_VALUE)
                .header(HttpHeaders.CONTENT_RANGE, format("bytes {}-{}/{}", start, end, resourceLength))
                .contentLength(rangeLength)
                .contentType(MediaType.parseMediaType(statObjectResponse.contentType()))
                .body(outputStream -> {
                    NioUtil.copyByNIO(objectResponse, outputStream, NioUtil.DEFAULT_LARGE_BUFFER_SIZE, null);
                    IoUtil.close(objectResponse);
                });
    }
}
