package com.jmsoftware.maf.authcenter.upload.service.impl;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.authcenter.upload.service.UploadService;
import com.jmsoftware.maf.common.exception.BusinessException;
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
 * <h1>UploadServiceImpl</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 2:20 PM
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {
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
        minioHelper.makeBucket(mediaBaseType);
        final var put = minioHelper.put(mediaBaseType, multipartFile.getOriginalFilename(), multipartFile);
        log.info("Uploaded single resource: {}/{}", put.bucket(), put.object());
        return String.format("%s/%s", put.bucket(), put.object());
    }
}
