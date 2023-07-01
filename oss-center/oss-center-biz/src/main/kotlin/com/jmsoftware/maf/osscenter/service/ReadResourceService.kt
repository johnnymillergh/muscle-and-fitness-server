package com.jmsoftware.maf.osscenter.service

import com.jmsoftware.maf.osscenter.response.SerializableStatObjectResponse
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

/**
 * # ReadResourceService
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:48 PM
 */
@Validated
interface ReadResourceService {
    /**
     * Get single resource, especially pictures, or other small size file.
     *
     * @param bucket the bucket
     * @param object the object
     * @return the response entity
     */
    fun asyncGetSingleResource(
        @NotBlank bucket: String,
        @NotBlank `object`: String
    ): ResponseEntity<StreamingResponseBody>

    /**
     * Stream single resource, more efficiency if the resource is large file, which will be streamed by range.
     * Responsive streaming
     *
     * @param bucket the bucket
     * @param object the object
     * @param range  the range
     * @return the response entity
     */
    fun asyncStreamSingleResource(
        @NotBlank bucket: String,
        @NotBlank `object`: String,
        range: String?
    ): ResponseEntity<StreamingResponseBody>

    /**
     * Async download single resource response entity.
     *
     * @param bucket the bucket
     * @param object the object
     * @return the response entity
     */
    fun asyncDownloadSingleResource(
        @NotBlank bucket: String,
        @NotBlank `object`: String
    ): ResponseEntity<StreamingResponseBody>

    /**
     * Gets resource information.
     *
     * @param bucket the bucket
     * @param object the object
     * @return the resource detail
     */
    fun stateObject(
        @NotBlank bucket: String,
        @NotBlank `object`: String
    ): SerializableStatObjectResponse
}
