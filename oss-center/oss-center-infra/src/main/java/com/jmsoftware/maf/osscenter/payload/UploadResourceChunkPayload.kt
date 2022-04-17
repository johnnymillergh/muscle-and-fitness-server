package com.jmsoftware.maf.osscenter.payload;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.jmsoftware.maf.osscenter.constant.Chunk.MAX_CHUNK_NUMBER;

/**
 * Description: UploadResourceChunkPayload, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/18/2021 9:29 AM
 **/
@Data
public class UploadResourceChunkPayload {
    private String bucket;
    @NotNull
    @Range(max = MAX_CHUNK_NUMBER)
    private Integer chunkNumber;
    @NotBlank
    @Pattern(regexp = "^[^<>:;,?\"*|/]+$")
    private String filename;
}
