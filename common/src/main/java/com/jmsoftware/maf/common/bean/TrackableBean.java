package com.jmsoftware.maf.common.bean;

import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * <h1>TrackableBean</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 3/12/22 1:17 AM
 **/
@Data
public class TrackableBean {
    /**
     * The Track. Pattern: [Zipkin trace id]#[Span id]
     */
    @Nullable
    private String track;
}
