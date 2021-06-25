package com.jmsoftware.maf.exercisemis.exercise.domain;

import com.jmsoftware.maf.common.bean.PaginationBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h1>ExercisePo</h1>
 * <p>
 * https://exrx.net/Lists/Directory
 *
 * @author 钟俊 (jun.zhong), e-mail: jun.zhong@ucarinc.com
 * @date 2/18/20 10:17 AM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExercisePo extends PaginationBase {
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
