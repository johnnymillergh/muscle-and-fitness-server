package com.jmsoftware.maf.osscenter.service;

import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse;
import com.jmsoftware.maf.osscenter.payload.MergeResourceChunkPayload;
import com.jmsoftware.maf.osscenter.payload.UploadResourceChunkPayload;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <h1>WriteResourceService</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 2:19 PM
 **/
@Validated
public interface WriteResourceService {
    /**
     * Upload single resource string.
     *
     * @param multipartFile the multipart file
     * @return the string
     */
    ObjectResponse uploadSingleResource(@NotNull MultipartFile multipartFile);

    /**
     * Upload resource chunk string.
     *
     * @param multipartFile the multipart file
     * @param payload       the payload
     * @return the string
     */
    ObjectResponse uploadResourceChunk(@NotNull MultipartFile multipartFile,
                                       @Valid @NotNull UploadResourceChunkPayload payload);

    /**
     * Merge resource chunk string.
     *
     * @param payload the payload
     * @return the string
     */
    ObjectResponse mergeResourceChunk(@Valid @NotNull MergeResourceChunkPayload payload);
}