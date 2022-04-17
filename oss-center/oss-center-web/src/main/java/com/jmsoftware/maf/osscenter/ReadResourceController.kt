package com.jmsoftware.maf.osscenter

import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.osscenter.response.SerializableStatObjectResponse
import com.jmsoftware.maf.osscenter.service.ReadResourceService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

/**
 * # ReadResourceController
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:41 AM
 */
@Validated
@RestController
class ReadResourceController(
    private val readResourceService: ReadResourceService
) {
    @GetMapping("/{bucket}/{object}")
    fun asyncGetSingleResource(
        @PathVariable bucket: String,
        @PathVariable `object`: String
    ): ResponseEntity<StreamingResponseBody> = readResourceService.asyncGetSingleResource(bucket, `object`)

    @GetMapping("/stream/{bucket}/{object}")
    fun asyncStreamSingleResource(
        @PathVariable bucket: String,
        @PathVariable `object`: String,
        @RequestHeader(name = HttpHeaders.RANGE, required = false) range: String
    ): ResponseEntity<StreamingResponseBody> = readResourceService.asyncStreamSingleResource(bucket, `object`, range)

    @GetMapping("/download/{bucket}/{object}")
    fun downloadSingleResource(
        @PathVariable bucket: String,
        @PathVariable `object`: String
    ): ResponseEntity<StreamingResponseBody> = readResourceService.asyncDownloadSingleResource(bucket, `object`)

    @GetMapping("/stat/{bucket}/{object}")
    fun statSingleResource(
        @PathVariable bucket: String,
        @PathVariable `object`: String
    ): ResponseBodyBean<SerializableStatObjectResponse> =
        ResponseBodyBean.ofSuccess(readResourceService.stateObject(bucket, `object`))
}
