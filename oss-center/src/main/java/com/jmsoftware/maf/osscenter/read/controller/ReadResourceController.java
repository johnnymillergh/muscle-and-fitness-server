package com.jmsoftware.maf.osscenter.read.controller;

import com.jmsoftware.maf.osscenter.read.service.ReadResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"Read resource API"})
public class ReadResourceController {
    private final ReadResourceService readResourceService;

    @GetMapping("/stream/{bucket}/{object}")
    @ApiOperation(value = "Stream single resource", notes = "Stream single resource")
    public ResponseEntity<StreamingResponseBody> asyncStreamSingleResource(@PathVariable String bucket,
                                                                           @PathVariable String object,
                                                                           @RequestHeader(name = HttpHeaders.RANGE,
                                                                                   required = false) String range) {
        return this.readResourceService.asyncStreamSingleResource(bucket, object, range);
    }

    @GetMapping("/download/{bucket}/{object}")
    @ApiOperation(value = "Download single resource", notes = "Download single resource")
    public ResponseEntity<StreamingResponseBody> downloadSingleResource(@PathVariable String bucket,
                                                                        @PathVariable String object) {
        return this.readResourceService.asyncDownloadSingleResource(bucket, object);
    }
}
