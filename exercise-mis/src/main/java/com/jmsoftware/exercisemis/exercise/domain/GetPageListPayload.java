package com.jmsoftware.exercisemis.exercise.domain;

import com.jmsoftware.common.bean.PaginationBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h1>GetPageListPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/18/20 2:27 PM
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class GetPageListPayload extends PaginationBase {
    /**
     * The ID of exercise.
     */
    private Long id;
    /**
     * The name of exercise.
     */
    private String name;
}
