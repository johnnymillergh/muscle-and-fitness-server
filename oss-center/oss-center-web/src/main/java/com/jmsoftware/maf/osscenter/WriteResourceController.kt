package com.jmsoftware.maf.osscenter

import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse
import com.jmsoftware.maf.osscenter.payload.MergeResourceChunkPayload
import com.jmsoftware.maf.osscenter.payload.UploadResourceChunkPayload
import com.jmsoftware.maf.osscenter.service.WriteResourceService
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

/**
 * <h1>WriteResourceController</h1>
 *
 *
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 1:57 PM
 */
@Validated
@RestController
class WriteResourceController(
    private val writeResourceService: WriteResourceService,
    private val messageSource: MessageSource
) {
    @PostMapping("/upload/single")
    fun uploadSingleResource(
        @RequestPart("file") multipartFile: MultipartFile
    ): ResponseBodyBean<ObjectResponse> = ResponseBodyBean.ofSuccess(
        writeResourceService.uploadSingleResource(multipartFile),
        messageSource.getMessage(
            "uploaded",
            null,
            LocaleContextHolder.getLocale()
        )
    )

    @PostMapping("/upload/chunk")
    fun uploadResourceChunk(
        @RequestPart("file") multipartFile: MultipartFile,
        @Valid payload: UploadResourceChunkPayload
    ): ResponseBodyBean<ObjectResponse> =
        ResponseBodyBean.ofSuccess(writeResourceService.uploadResourceChunk(multipartFile, payload))

    @PutMapping("/merge/chunk")
    fun mergeResourceChunk(
        @Valid @RequestBody payload: MergeResourceChunkPayload
    ): ResponseBodyBean<ObjectResponse> = ResponseBodyBean.ofSuccess(writeResourceService.mergeResourceChunk(payload))
}
