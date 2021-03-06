package com.jmsoftware.maf.mafmis.exercise.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.maf.common.exception.BusinessException;
import com.jmsoftware.maf.mafmis.exercise.domain.ExercisePo;
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
    public ExercisePo queryById(Long id) {
        if (ObjectUtil.isNull(id)) {
            throw new BusinessException("Cannot execute the query! Cause: the ID is null.");
        }
        return exerciseMapper.selectById(id);
    }

    @Override
    public List<ExercisePo> getPageList(ExercisePo exercisePo) {
        var page = new Page<ExercisePo>(exercisePo.getCurrentPage(), exercisePo.getPageSize());
        exerciseMapper.selectAll(exercisePo, page);
        log.info("Total pages: {}", page.getPages());
        return page.getRecords();
    }

    @Override
    public ExercisePo insert(ExercisePo exercisePo) {
        exerciseMapper.insert(exercisePo);
        return exercisePo;
    }

    @Override
    public ExercisePo update(ExercisePo exercisePo) {
        exerciseMapper.update(exercisePo);
        return this.queryById(exercisePo.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        return exerciseMapper.deleteById(id) > 0;
    }
}
