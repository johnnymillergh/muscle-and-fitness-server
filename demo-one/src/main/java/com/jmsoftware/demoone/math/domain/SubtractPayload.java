package com.jmsoftware.demoone.math.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <h1>SubtractPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/17/20 8:46 AM
 **/
@Data
public class SubtractPayload {
    @NotNull
    private Double parameter1;
    @NotNull
    private Double parameter2;
}
