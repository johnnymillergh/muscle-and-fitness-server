package com.jmsoftware.maf.osscenter.payload

import com.jmsoftware.maf.osscenter.constant.Chunk
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Range

/**
 * # UploadResourceChunkPayload
 *
 * Description: UploadResourceChunkPayload, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/18/2021 9:29 AM
 */
class UploadResourceChunkPayload {
    var bucket: String? = null
    @NotNull
    @Range(max = Chunk.MAX_CHUNK_NUMBER.toLong())
    var chunkNumber: Int? = null
    @NotBlank
    @Pattern(regexp = "^[^<>:;,?\"*|/]+$")
    var filename:  String? = null
}
