package com.jmsoftware.maf.common.domain;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <h1>ValidationTestPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/14/20 11:34 AM
 **/
@Data
public class ValidationTestPayload {
    @NotNull
    @Min(value = 1L)
    private Long id;
    @NotBlank
    private String name;
}
