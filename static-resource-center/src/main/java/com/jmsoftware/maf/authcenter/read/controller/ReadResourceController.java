package com.jmsoftware.maf.authcenter.read.controller;

import com.jmsoftware.maf.authcenter.read.entity.GetSingleResourcePayload;
import com.jmsoftware.maf.authcenter.read.service.ReadResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @GetMapping("/{bucket}/{object}")
    @ApiOperation(value = "Get single resource", notes = "Get or download single resource")
    public ResponseEntity<Resource> getSingleResource(@PathVariable String bucket, @PathVariable String object,
                                                      @Valid GetSingleResourcePayload payload) {
        return readResourceService.getSingleResource(bucket, object, payload);
    }
}
