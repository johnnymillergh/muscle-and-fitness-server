package com.jmsoftware.maf.osscenter.read.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

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
     * Stream single resource.
     *
     * @param bucket the bucket
     * @param object the object
     * @param range  the range
     * @return the single resource
     */
    ResponseEntity<Resource> streamSingleResource(@NotBlank String bucket, @NotBlank String object,
                                                  @Nullable String range);

    /**
     * Download single resource response entity.
     *
     * @param bucket the bucket
     * @param object the object
     * @return the response entity
     */
    ResponseEntity<Resource> downloadSingleResource(@NotBlank String bucket, @NotBlank String object);
}
