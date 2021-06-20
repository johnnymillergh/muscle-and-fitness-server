package com.jmsoftware.maf.authcenter.upload.controller;

import com.jmsoftware.maf.authcenter.upload.service.UploadService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <h1>UploadController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 1:57 PM
 **/
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class UploadController {
    private final UploadService uploadService;

    @PostMapping("/single")
    @SneakyThrows({IOException.class, BusinessException.class})
    public ResponseBodyBean<String> uploadSingleResource(@RequestParam("file") MultipartFile multipartFile) {
        return ResponseBodyBean.ofSuccess(uploadService.uploadSingleResource(multipartFile), "Succeed to upload");
    }
}
