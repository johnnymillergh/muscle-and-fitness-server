package com.jmsoftware.maf.mafmis.exercise.service;

import com.jmsoftware.maf.mafmis.exercise.persistence.Exercise;

import java.util.List;

/**
 * <h1>ExerciseService</h1>
 * <p>
 * Exercise Service
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2/18/20 10:21 AM
 */
public interface ExerciseService {
    /**
     * Query by id exercise po.
     *
     * @param id the id
     * @return the exercise po
     */
    Exercise queryById(Long id);

    /**
     * Gets page list.
     *
     * @param exercisePo the exercise po
     * @return the page list
     */
    List<Exercise> getPageList(Exercise exercisePo);

    /**
     * Insert exercise po.
     *
     * @param exercisePo the exercise po
     * @return the exercise po
     */
    Exercise insert(Exercise exercisePo);

    /**
     * Update exercise po.
     *
     * @param exercisePo the exercise po
     * @return the exercise po
     */
    Exercise update(Exercise exercisePo);

    /**
     * Delete by id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean deleteById(Long id);
}
