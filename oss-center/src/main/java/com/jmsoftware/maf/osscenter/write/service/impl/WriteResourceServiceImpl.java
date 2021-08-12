package com.jmsoftware.maf.osscenter.write.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.jmsoftware.maf.common.exception.BizException;
import com.jmsoftware.maf.osscenter.write.entity.MergeResourceChunkPayload;
import com.jmsoftware.maf.osscenter.write.entity.ObjectResponse;
import com.jmsoftware.maf.osscenter.write.service.WriteResourceService;
import com.jmsoftware.maf.springcloudstarter.minio.MinioHelper;
import io.minio.ComposeSource;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    public ObjectResponse uploadSingleResource(@NotNull MultipartFile multipartFile) {
        val mediaType = this.parseMediaType(multipartFile);
        this.minioHelper.makeBucket(mediaType.getType());
        val objectWriteResponse = this.minioHelper.put(mediaType.getType(), multipartFile.getOriginalFilename(),
                                                       multipartFile);
        val objectResponse = new ObjectResponse();
        objectResponse.setBucket(objectWriteResponse.bucket());
        objectResponse.setObject(objectWriteResponse.object());
        objectResponse.setEtag(objectWriteResponse.etag());
        log.info("Uploaded single resource. {}", objectResponse);
        return objectResponse;
    }

    @Override
    @SneakyThrows
    public ObjectResponse uploadResourceChunk(@NotNull MultipartFile multipartFile,
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
        val objectResponse = new ObjectResponse();
        objectResponse.setBucket(mediaType.getType());
        objectResponse.setObject(orderedFilename);
        if (ObjectUtil.isNotNull(statObjectResponse)) {
            val md5Hex = DigestUtil.md5Hex(multipartFile.getInputStream());
            if (StrUtil.equalsIgnoreCase(md5Hex, statObjectResponse.etag())) {
                log.warn(
                        "Found previously uploaded file, skip uploading chunk. Filename: {}, statObjectResponse: {}, " +
                                "MD5: {}", orderedFilename, statObjectResponse, md5Hex);
                objectResponse.setEtag(statObjectResponse.etag());
                return objectResponse;
            }
        }
        this.minioHelper.makeBucket(mediaType.getType());
        val objectWriteResponse = this.minioHelper.put(mediaType.getType(), orderedFilename, multipartFile);
        log.info("Uploaded resource chunk. {}", objectResponse);
        objectResponse.setEtag(objectWriteResponse.etag());
        return objectResponse;
    }

    @Override
    public ObjectResponse mergeResourceChunk(@Valid @NotNull MergeResourceChunkPayload payload) {
        val objectName = this.validateObject(payload.getObjectList());
        val sources = payload.getObjectList()
                .stream()
                .map(object -> ComposeSource.builder().bucket(payload.getBucket()).object(object).build())
                .collect(Collectors.toList());
        val statObjectResponse = this.minioHelper.statObject(payload.getBucket(),
                                                             CollUtil.getFirst(payload.getObjectList()));
        val headers = new HashMap<String, String>(4);
        headers.put("Content-Type", statObjectResponse.contentType());
        val objectWriteResponse = this.minioHelper.composeObject(payload.getBucket(), objectName, sources, headers);
        val objectResponse = new ObjectResponse();
        objectResponse.setBucket(objectWriteResponse.bucket());
        objectResponse.setObject(objectWriteResponse.object());
        objectResponse.setEtag(objectWriteResponse.etag());
        log.info("Merged resource chunks. {}", objectResponse);
        return objectResponse;
    }

    private String validateObject(List<String> objectList) {
        val objectNameSet = objectList.stream().map(object -> {
            val lastIndexOfDot = StrUtil.lastIndexOfIgnoreCase(object, ".");
            return StrUtil.subPre(object, lastIndexOfDot);
        }).collect(Collectors.toSet());
        if (CollUtil.size(objectNameSet) != 1) {
            log.error("Object list is not valid! {}", objectNameSet);
            throw new IllegalArgumentException("Object list is not valid");
        }
        return objectNameSet.iterator().next();
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
