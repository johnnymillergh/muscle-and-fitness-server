package com.jmsoftware.exercisemis.exercise.service;

import com.jmsoftware.exercisemis.exercise.domain.ExercisePo;

import java.util.List;

/**
 * <h1>ExerciseService</h1>
 * <p>
 * Exercise Service
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 2/18/20 10:21 AM
 */
public interface ExerciseService {
    /**
     * Query by id exercise po.
     *
     * @param id the id
     * @return the exercise po
     */
    ExercisePo queryById(Long id);

    /**
     * Gets page list.
     *
     * @param exercisePo the exercise po
     * @return the page list
     */
    List<ExercisePo> getPageList(ExercisePo exercisePo);

    /**
     * Insert exercise po.
     *
     * @param exercisePo the exercise po
     * @return the exercise po
     */
    ExercisePo insert(ExercisePo exercisePo);

    /**
     * Update exercise po.
     *
     * @param exercisePo the exercise po
     * @return the exercise po
     */
    ExercisePo update(ExercisePo exercisePo);

    /**
     * Delete by id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean deleteById(Long id);
}
