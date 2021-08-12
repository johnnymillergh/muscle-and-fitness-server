package com.jmsoftware.maf.osscenter.write.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.jmsoftware.maf.common.exception.BizException;
import com.jmsoftware.maf.osscenter.write.service.WriteResourceService;
import com.jmsoftware.maf.springcloudstarter.minio.MinioHelper;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * <h1>WriteResourceServiceImpl</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 2:20 PM
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class WriteResourceServiceImpl implements WriteResourceService {
    private final MinioHelper minioHelper;

    @Override
    @SneakyThrows
    public String uploadSingleResource(@NotNull MultipartFile multipartFile) {
        val mediaType = this.parseMediaType(multipartFile);
        val bucketMade = this.minioHelper.makeBucket(mediaType.getType());
        val objectWriteResponse = this.minioHelper.put(mediaType.getType(), multipartFile.getOriginalFilename(),
                                                       multipartFile);
        log.info("Uploaded single resource: {}/{}. bucketMade: {}", objectWriteResponse.bucket(),
                 objectWriteResponse.object(), bucketMade);
        return String.format("%s/%s", objectWriteResponse.bucket(), objectWriteResponse.object());
    }

    @Override
    @SneakyThrows
    public String uploadResourceChunk(@NotNull MultipartFile multipartFile,
                                      @NotNull @Range(max = MAX_CHUNK_NUMBER) Integer chunkNumber) {
        if (StrUtil.isBlank(multipartFile.getOriginalFilename())) {
            throw new IllegalArgumentException("File name required");
        }
        val mediaType = this.parseMediaType(multipartFile);
        val orderedFilename = String.format("%s.chunk%s", multipartFile.getOriginalFilename(),
                                            NumberUtil.decimalFormat("000", chunkNumber));
        StatObjectResponse statObjectResponse = null;
        try {
            statObjectResponse = this.minioHelper.statObject(mediaType.getType(), orderedFilename);
        } catch (Exception e) {
            log.error("Exception occurred when looking for object. Exception message: {}", e.getMessage());
        }
        if (ObjectUtil.isNotNull(statObjectResponse)) {
            val md5Hex = DigestUtil.md5Hex(multipartFile.getInputStream());
            if (StrUtil.equalsIgnoreCase(md5Hex, statObjectResponse.etag())) {
                log.warn(
                        "Found previously uploaded file, skip uploading chunk. Filename: {}, statObjectResponse: {}, " +
                                "MD5: {}", orderedFilename, statObjectResponse, md5Hex);
                return statObjectResponse.etag();
            }
        }
        val bucketMade = this.minioHelper.makeBucket(mediaType.getType());
        val objectWriteResponse = this.minioHelper.put(mediaType.getType(), orderedFilename, multipartFile);
        log.info("Uploaded resource chunk: {}/{}. bucketMade: {}, etag (MD5): {}", objectWriteResponse.bucket(),
                 objectWriteResponse.object(), bucketMade, objectWriteResponse.etag());
        return objectWriteResponse.etag();
    }

    private MediaType parseMediaType(MultipartFile multipartFile) throws IOException, BizException {
        val tika = new Tika();
        val detectedMediaType = tika.detect(multipartFile.getInputStream());
        log.info("Detected media type: {}", detectedMediaType);
        if (StrUtil.isBlank(detectedMediaType)) {
            throw new BizException("Media extension detection failed!");
        }
        return MediaType.parse(detectedMediaType);
    }
}
