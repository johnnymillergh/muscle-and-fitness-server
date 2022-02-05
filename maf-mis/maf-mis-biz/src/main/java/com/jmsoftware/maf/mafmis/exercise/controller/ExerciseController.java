package com.jmsoftware.maf.mafmis.exercise.controller;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.mafmis.exercise.payload.GetPageListPayload;
import com.jmsoftware.maf.mafmis.exercise.persistence.Exercise;
import com.jmsoftware.maf.mafmis.exercise.service.ExerciseService;
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
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping("/get-by-id")
    public ResponseBodyBean<Exercise> selectOne(Long id) {
        return ResponseBodyBean.ofSuccess(this.exerciseService.queryById(id));
    }

    @GetMapping("/get-page-list")
    public ResponseBodyBean<List<Exercise>> getPageList(@Valid GetPageListPayload payload) {
        return ResponseBodyBean.ofSuccess(this.exerciseService.getPageList(payload));
    }
}
