package com.jmsoftware.maf.mafmis.exercise.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.maf.common.exception.BizException;
import com.jmsoftware.maf.mafmis.exercise.persistence.Exercise;
import com.jmsoftware.maf.mafmis.exercise.mapper.ExerciseMapper;
import com.jmsoftware.maf.mafmis.exercise.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <h1>ExerciseServiceImpl</h1>
 * <p>
 * Exercise Service Impl
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2/18/20 10:19 AM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseMapper exerciseMapper;

    @Override
    @SneakyThrows
    public Exercise queryById(Long id) {
        if (ObjectUtil.isNull(id)) {
            throw new BizException("Cannot execute the query! Cause: the ID is null.");
        }
        return this.exerciseMapper.selectById(id);
    }

    @Override
    public List<Exercise> getPageList(Exercise exercisePo) {
        var page = new Page<Exercise>(exercisePo.getCurrentPage(), exercisePo.getPageSize());
        this.exerciseMapper.selectAll(exercisePo, page);
        log.info("Total pages: {}", page.getPages());
        return page.getRecords();
    }

    @Override
    public Exercise insert(Exercise exercisePo) {
        this.exerciseMapper.insert(exercisePo);
        return exercisePo;
    }

    @Override
    public Exercise update(Exercise exercisePo) {
        this.exerciseMapper.update(exercisePo);
        return this.queryById(exercisePo.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        return this.exerciseMapper.deleteById(id) > 0;
    }
}
