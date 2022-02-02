package com.jmsoftware.maf.osscenter.read.controller;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.osscenter.read.entity.SerializableStatObjectResponse;
import com.jmsoftware.maf.osscenter.read.service.ReadResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * <h1>ReadResourceController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 5:16 PM
 **/
@Validated
@RestController
@RequiredArgsConstructor
public class ReadResourceController {
    private final ReadResourceService readResourceService;

    @GetMapping("/{bucket}/{object}")
    public ResponseEntity<StreamingResponseBody> asyncGetSingleResource(
            @PathVariable String bucket,
            @PathVariable String object
    ) {
        return this.readResourceService.asyncGetSingleResource(bucket, object);
    }

    @GetMapping("/stream/{bucket}/{object}")
    public ResponseEntity<StreamingResponseBody> asyncStreamSingleResource(
            @PathVariable String bucket,
            @PathVariable String object,
            @RequestHeader(name = HttpHeaders.RANGE, required = false) String range
    ) {
        return this.readResourceService.asyncStreamSingleResource(bucket, object, range);
    }

    @GetMapping("/download/{bucket}/{object}")
    public ResponseEntity<StreamingResponseBody> downloadSingleResource(
            @PathVariable String bucket,
            @PathVariable String object
    ) {
        return this.readResourceService.asyncDownloadSingleResource(bucket, object);
    }

    @GetMapping("/stat/{bucket}/{object}")
    public ResponseBodyBean<SerializableStatObjectResponse> statSingleResource(
            @PathVariable String bucket,
            @PathVariable String object
    ) {
        return ResponseBodyBean.ofSuccess(this.readResourceService.stateObject(bucket, object));
    }
}
