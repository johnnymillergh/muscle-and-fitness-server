package com.jmsoftware.maf.mafmis.exercise.controller

import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.mafmis.exercise.payload.GetPageListPayload
import com.jmsoftware.maf.mafmis.exercise.persistence.Exercise
import com.jmsoftware.maf.mafmis.exercise.service.ExerciseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * # ExerciseController
 *
 * Exercise Controller
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:46 AM
 */
@RestController
@RequestMapping("/exercise")
class ExerciseController(
    private val exerciseService: ExerciseService
) {
    @GetMapping("/get-by-id")
    fun selectOne(id: Long): ResponseBodyBean<Exercise> {
        return ResponseBodyBean.ofSuccess(exerciseService.queryById(id))
    }

    @GetMapping("/get-page-list")
    fun getPageList(payload: @Valid GetPageListPayload): ResponseBodyBean<List<Exercise>> {
        return ResponseBodyBean.ofSuccess(exerciseService.getPageList(payload))
    }
}
