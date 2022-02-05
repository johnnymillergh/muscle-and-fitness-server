package com.jmsoftware.maf.mafmis.exercise.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h1>Exercise</h1>
 * <p>
 * https://exrx.net/Lists/Directory
 *
 * @author 钟俊 (jun.zhong), e-mail: jun.zhong@ucarinc.com
 * @date 2/18/20 10:17 AM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Exercise {
    /**
     * The ID of exercise.
     */
    private Long id;
    /**
     * The name of exercise.
     */
    private String name;
    /**
     * Exercise preparation description.
     */
    private String preparation;
    /**
     * Exercise execution description.
     */
    private String execution;
    /**
     * Exercise GIF image path.
     */
    private String exerciseGifPath;
}
