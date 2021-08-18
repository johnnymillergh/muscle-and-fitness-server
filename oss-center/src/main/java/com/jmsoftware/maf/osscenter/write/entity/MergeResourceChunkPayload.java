package com.jmsoftware.maf.osscenter.write.entity;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Description: MergeResourceChunkPayload, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/12/2021 8:48 PM
 **/
@Data
public class MergeResourceChunkPayload {
    @NotBlank
    private String bucket;
    @NotEmpty
    private List<@Valid @NotBlank String> objectList;
}
