package com.jmsoftware.maf.osscenter.payload

import com.jmsoftware.maf.osscenter.constant.Chunk
import org.hibernate.validator.constraints.Range
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

/**
 * # UploadResourceChunkPayload
 *
 * Description: UploadResourceChunkPayload, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/18/2021 9:29 AM
 */
class UploadResourceChunkPayload {
    var bucket: String? = null
    var chunkNumber: @NotNull @Range(max = Chunk.MAX_CHUNK_NUMBER.toLong()) Int? = null
    var filename: @NotBlank @Pattern(regexp = "^[^<>:;,?\"*|/]+$") String? = null
}
