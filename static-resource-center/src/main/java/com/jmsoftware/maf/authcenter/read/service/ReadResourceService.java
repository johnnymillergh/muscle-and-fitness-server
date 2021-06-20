package com.jmsoftware.maf.authcenter.read.service;

import com.jmsoftware.maf.authcenter.read.entity.GetSingleResourcePayload;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    /**
     * Gets single resource.
     *
     * @param bucket  the bucket
     * @param object  the object
     * @param payload the payload
     * @return the single resource
     */
    ResponseEntity<Resource> getSingleResource(@NotBlank String bucket, @NotBlank String object,
                                               @Valid @NotNull GetSingleResourcePayload payload);
}
