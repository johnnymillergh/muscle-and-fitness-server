package com.jmsoftware.maf.mafmis.exercise.controller;

import cn.hutool.core.bean.BeanUtil;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.mafmis.exercise.domain.ExercisePo;
import com.jmsoftware.maf.mafmis.exercise.domain.GetPageListPayload;
import com.jmsoftware.maf.mafmis.exercise.service.ExerciseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <h1>ExerciseController</h1>
 * <p>
 * Exercise Controller
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2/18/20 10:22 AM
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise")
@Api(tags = {"Exercise Controller"})
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping("/get-by-id")
    @ApiOperation(value = "/get-by-id", notes = "Retrieve exercise by id")
    public ResponseBodyBean<ExercisePo> selectOne(Long id) {
        return ResponseBodyBean.ofSuccess(exerciseService.queryById(id));
    }

    @GetMapping("/get-page-list")
    @ApiOperation(value = "/get-page-list", notes = "Retrieve page list")
    public ResponseBodyBean<List<ExercisePo>> getPageList(@Valid GetPageListPayload payload) {
        var exercisePo = new ExercisePo();
        BeanUtil.copyProperties(payload, exercisePo);
        return ResponseBodyBean.ofSuccess(exerciseService.getPageList(exercisePo));
    }
}
