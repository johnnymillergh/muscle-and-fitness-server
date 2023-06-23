package com.jmsoftware.maf.osscenter.payload

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

/**
 * # MergeResourceChunkPayload
 *
 * Description: MergeResourceChunkPayload, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/17/22 8:34 AM
 */
class MergeResourceChunkPayload {
    lateinit var bucket: @NotBlank String
    lateinit var objectList: @NotEmpty List<String>
}
