package com.jmsoftware.maf.mafmis.exercise.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.maf.mafmis.exercise.persistence.Exercise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <h1>ExerciseMapper</h1>
 * <p>
 * Exercise Mapper
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2/18/20 10:22 AM
 */
@Mapper
public interface ExerciseMapper {
    /**
     * Select by id exercise po.
     *
     * @param id the id
     * @return the exercise po
     */
    Exercise selectById(Long id);

    /**
     * Select all page.
     *
     * @param exercisePo the exercise po
     * @param page       the page
     * @return the page
     */
    IPage<Exercise> selectAll(@Param("exercise") Exercise exercisePo, Page<Exercise> page);

    /**
     * Insert int.
     *
     * @param exercisePo the exercise po
     * @return the int
     */
    int insert(Exercise exercisePo);

    /**
     * Update int.
     *
     * @param exercise the exercise po
     * @return the int
     */
    int update(Exercise exercise);

    /**
     * Delete by id int.
     *
     * @param id the id
     * @return the int
     */
    int deleteById(Long id);
}
