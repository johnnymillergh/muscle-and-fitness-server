package com.jmsoftware.maf.osscenter.write.service;

import com.jmsoftware.maf.osscenter.write.entity.MergeResourceChunkPayload;
import com.jmsoftware.maf.osscenter.write.entity.ObjectResponse;
import org.hibernate.validator.constraints.Range;
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
    long MAX_CHUNK_NUMBER = 999;

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
     * @param chunkNumber   the chunk number
     * @return the string
     */
    ObjectResponse uploadResourceChunk(@NotNull MultipartFile multipartFile,
                                       @NotNull @Range(max = MAX_CHUNK_NUMBER) Integer chunkNumber);

    /**
     * Merge resource chunk string.
     *
     * @param payload the payload
     * @return the string
     */
    ObjectResponse mergeResourceChunk(@Valid @NotNull MergeResourceChunkPayload payload);
}
