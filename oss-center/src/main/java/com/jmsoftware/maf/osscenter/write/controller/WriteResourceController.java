package com.jmsoftware.maf.osscenter.write.controller;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.exception.BizException;
import com.jmsoftware.maf.osscenter.write.service.WriteResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <h1>WriteResourceController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 1:57 PM
 **/
@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = {"Write resource API"})
public class WriteResourceController {
    private final WriteResourceService writeResourceService;
    private final MessageSource messageSource;

    @PostMapping("/upload/single")
    @SneakyThrows({IOException.class, BizException.class})
    @ApiOperation(value = "Upload single resource", notes = "Upload single resource")
    public ResponseBodyBean<String> uploadSingleResource(@RequestParam("file") MultipartFile multipartFile) {
        return ResponseBodyBean.ofSuccess(this.writeResourceService.uploadSingleResource(multipartFile),
                                          this.messageSource.getMessage("uploaded", null,
                                                                        LocaleContextHolder.getLocale()));
    }
}
