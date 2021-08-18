package com.jmsoftware.maf.osscenter.write.entity;

import com.jmsoftware.maf.osscenter.write.service.WriteResourceService;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Description: UploadResourceChunkPayload, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/18/2021 9:29 AM
 **/
@Data
public class UploadResourceChunkPayload {
    private String bucket;
    @NotNull
    @Range(max = WriteResourceService.MAX_CHUNK_NUMBER)
    private Integer chunkNumber;
    @NotBlank
    @Pattern(regexp = "^[^<>:;,?\"*|/]+$")
    private String filename;
}
