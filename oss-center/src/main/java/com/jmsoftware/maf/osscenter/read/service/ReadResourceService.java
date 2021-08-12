package com.jmsoftware.maf.osscenter.read.service;

import com.jmsoftware.maf.osscenter.read.entity.SerializableStatObjectResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.unit.DataSize;
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
    String BUCKET_OBJECT_NAME_REGEX = "^.+/.+$";
    DataSize SMALL_CHUNK_SIZE = DataSize.ofMegabytes(1);
    DataSize MEDIUM_CHUNK_SIZE = DataSize.ofMegabytes(4);
    DataSize LARGE_CHUNK_SIZE = DataSize.ofMegabytes(8);

    /**
     * Async stream single resource response entity.
     *
     * @param bucket the bucket
     * @param object the object
     * @param range  the range
     * @return the response entity
     */
    ResponseEntity<StreamingResponseBody> asyncStreamSingleResource(@NotBlank String bucket, @NotBlank String object,
                                                                    @Nullable String range);

    /**
     * Async download single resource response entity.
     *
     * @param bucket the bucket
     * @param object the object
     * @return the response entity
     */
    ResponseEntity<StreamingResponseBody> asyncDownloadSingleResource(@NotBlank String bucket, @NotBlank String object);

    /**
     * Gets resource information.
     *
     * @param bucket the bucket
     * @param object the object
     * @return the resource detail
     */
    SerializableStatObjectResponse stateObject(@NotBlank String bucket, @NotBlank String object);
}
