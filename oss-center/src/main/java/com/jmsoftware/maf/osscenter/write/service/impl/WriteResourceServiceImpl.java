package com.jmsoftware.maf.osscenter.write.service.impl;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.common.exception.BusinessException;
import com.jmsoftware.maf.osscenter.write.service.WriteResourceService;
import com.jmsoftware.maf.springcloudstarter.helper.MinioHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
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
    public String uploadSingleResource(@NotNull MultipartFile multipartFile) throws IOException, BusinessException {
        val tika = new Tika();
        val detectedMediaType = tika.detect(multipartFile.getInputStream());
        log.info("Detected media type: {}", detectedMediaType);
        if (StrUtil.isBlank(detectedMediaType)) {
            throw new BusinessException("Media extension detection failed!");
        }
        val mediaType = MediaType.parse(detectedMediaType);
        val mediaBaseType = mediaType.getType();
        val bucketMade = this.minioHelper.makeBucket(mediaBaseType);
        val objectWriteResponse = this.minioHelper.put(mediaBaseType, multipartFile.getOriginalFilename(),
                                                       multipartFile);
        log.info("Uploaded single resource: {}/{}. bucketMade: {}", objectWriteResponse.bucket(),
                 objectWriteResponse.object(), bucketMade);
        return String.format("%s/%s", objectWriteResponse.bucket(), objectWriteResponse.object());
    }
}
