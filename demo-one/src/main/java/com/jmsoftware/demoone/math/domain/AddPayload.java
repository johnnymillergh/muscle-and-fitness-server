package com.jmsoftware.demoone.math.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <h1>AddPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/17/20 8:21 AM
 **/
@Data
public class AddPayload {
    @NotEmpty
    private List<Double> parameterList;
}
