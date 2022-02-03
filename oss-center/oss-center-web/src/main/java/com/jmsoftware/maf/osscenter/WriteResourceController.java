package com.jmsoftware.maf.osscenter;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse;
import com.jmsoftware.maf.osscenter.payload.MergeResourceChunkPayload;
import com.jmsoftware.maf.osscenter.payload.UploadResourceChunkPayload;
import com.jmsoftware.maf.osscenter.service.WriteResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * <h1>WriteResourceController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 1:57 PM
 **/
@Validated
@RestController
@RequiredArgsConstructor
public class WriteResourceController {
    private final WriteResourceService writeResourceService;
    private final MessageSource messageSource;

    @PostMapping("/upload/single")
    public ResponseBodyBean<ObjectResponse> uploadSingleResource(
            @RequestPart("file") MultipartFile multipartFile
    ) {
        return ResponseBodyBean.ofSuccess(
                this.writeResourceService.uploadSingleResource(multipartFile),
                this.messageSource.getMessage(
                        "uploaded",
                        null,
                        LocaleContextHolder.getLocale()
                )
        );
    }

    @PostMapping("/upload/chunk")
    public ResponseBodyBean<ObjectResponse> uploadResourceChunk(
            @RequestPart("file") MultipartFile multipartFile,
            @Valid UploadResourceChunkPayload payload
    ) {
        return ResponseBodyBean.ofSuccess(this.writeResourceService.uploadResourceChunk(multipartFile, payload));
    }

    @PutMapping("/merge/chunk")
    public ResponseBodyBean<ObjectResponse> mergeResourceChunk(
            @Valid @RequestBody MergeResourceChunkPayload payload
    ) {
        return ResponseBodyBean.ofSuccess(this.writeResourceService.mergeResourceChunk(payload));
    }
}
