package com.jmsoftware.maf.exercisemis.exercise.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.maf.exercisemis.exercise.domain.ExercisePo;
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
    ExercisePo selectById(Long id);

    /**
     * Select all page.
     *
     * @param exercisePo the exercise po
     * @param page       the page
     * @return the page
     */
    IPage<ExercisePo> selectAll(@Param("exercise") ExercisePo exercisePo, Page<ExercisePo> page);

    /**
     * Insert int.
     *
     * @param exercisePo the exercise po
     * @return the int
     */
    int insert(ExercisePo exercisePo);

    /**
     * Update int.
     *
     * @param exercisePo the exercise po
     * @return the int
     */
    int update(ExercisePo exercisePo);

    /**
     * Delete by id int.
     *
     * @param id the id
     * @return the int
     */
    int deleteById(Long id);
}
