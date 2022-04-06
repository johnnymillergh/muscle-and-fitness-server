package com.jmsoftware.maf.osscenter.service;

import com.jmsoftware.maf.osscenter.response.SerializableStatObjectResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotBlank;

/**
 * <h1>ReadResourceService</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 5:17 PM
 **/
@Validated
public interface ReadResourceService {
    /**
     * Get single resource, especially pictures, or other small size file.
     *
     * @param bucket the bucket
     * @param object the object
     * @return the response entity
     */
    ResponseEntity<StreamingResponseBody> asyncGetSingleResource(
            @NotBlank String bucket,
            @NotBlank String object
    );

    /**
     * Stream single resource, more efficiency if the resource is large file, which will be streamed by range.
     * Responsive streaming
     *
     * @param bucket the bucket
     * @param object the object
     * @param range  the range
     * @return the response entity
     */
    ResponseEntity<StreamingResponseBody> asyncStreamSingleResource(
            @NotBlank String bucket,
            @NotBlank String object,
            @Nullable String range
    );

    /**
     * Async download single resource response entity.
     *
     * @param bucket the bucket
     * @param object the object
     * @return the response entity
     */
    ResponseEntity<StreamingResponseBody> asyncDownloadSingleResource(
            @NotBlank String bucket,
            @NotBlank String object
    );

    /**
     * Gets resource information.
     *
     * @param bucket the bucket
     * @param object the object
     * @return the resource detail
     */
    SerializableStatObjectResponse stateObject(
            @NotBlank String bucket,
            @NotBlank String object
    );
}
