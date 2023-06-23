package com.jmsoftware.maf.osscenter.service

import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse
import com.jmsoftware.maf.osscenter.payload.MergeResourceChunkPayload
import com.jmsoftware.maf.osscenter.payload.UploadResourceChunkPayload
import org.springframework.validation.annotation.Validated
import org.springframework.web.multipart.MultipartFile
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

/**
 * # WriteResourceService
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:49 PM
 */
@Validated
interface WriteResourceService {
    /**
     * Upload single resource string.
     *
     * @param multipartFile the multipart file
     * @return the string
     */
    fun uploadSingleResource(multipartFile: @NotNull MultipartFile): ObjectResponse

    /**
     * Upload resource chunk string.
     *
     * @param multipartFile the multipart file
     * @param payload       the payload
     * @return the string
     */
    fun uploadResourceChunk(
        multipartFile: @NotNull MultipartFile,
        payload: @Valid @NotNull UploadResourceChunkPayload
    ): ObjectResponse

    /**
     * Merge resource chunk string.
     *
     * @param payload the payload
     * @return the string
     */
    fun mergeResourceChunk(payload: @Valid @NotNull MergeResourceChunkPayload): ObjectResponse
}
