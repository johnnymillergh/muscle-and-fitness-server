package com.jmsoftware.maf.osscenter.write.controller;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.osscenter.write.entity.MergeResourceChunkPayload;
import com.jmsoftware.maf.osscenter.write.entity.ObjectResponse;
import com.jmsoftware.maf.osscenter.write.service.WriteResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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
    @ApiOperation(value = "Upload single resource", notes = "Upload single resource")
    public ResponseBodyBean<ObjectResponse> uploadSingleResource(@RequestParam("file") MultipartFile multipartFile) {
        return ResponseBodyBean.ofSuccess(this.writeResourceService.uploadSingleResource(multipartFile),
                                          this.messageSource.getMessage("uploaded", null,
                                                                        LocaleContextHolder.getLocale()));
    }

    @PostMapping("/upload/chunk/{chunkNumber}")
    @ApiOperation(value = "Upload chunk of resource", notes = "Upload chunk of resource")
    public ResponseBodyBean<ObjectResponse> uploadResourceChunk(@RequestParam("file") MultipartFile multipartFile,
                                                                @RequestParam(required = false) String bucket,
                                                                @PathVariable Integer chunkNumber) {
        return ResponseBodyBean.ofSuccess(
                this.writeResourceService.uploadResourceChunk(multipartFile, bucket, chunkNumber));
    }

    @PutMapping("/merge/chunk")
    @ApiOperation(value = "Merge chunk of resource", notes = "Merge chunk of resource")
    public ResponseBodyBean<ObjectResponse> mergeResourceChunk(@Valid @RequestBody MergeResourceChunkPayload payload) {
        return ResponseBodyBean.ofSuccess(this.writeResourceService.mergeResourceChunk(payload));
    }
}
