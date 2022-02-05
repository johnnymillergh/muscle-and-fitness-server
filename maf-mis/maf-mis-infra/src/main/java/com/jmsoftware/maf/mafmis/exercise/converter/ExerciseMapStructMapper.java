/*
 * Copyright By ZATI
 * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
 * All rights reserved.
 *
 * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or
 * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your
 * system.
 */
package com.jmsoftware.maf.mafmis.exercise.converter;

import com.jmsoftware.maf.mafmis.exercise.payload.GetPageListPayload;
import com.jmsoftware.maf.mafmis.exercise.persistence.Exercise;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

/**
 * Description: ExerciseMapStructMapper, change description here.
 *
 * @author 钟俊 (za-zhongjun), email: jun.zhong@zatech.com, date: 2/5/2022 5:51 PM
 **/
@Mapper
public interface ExerciseMapStructMapper {
    ExerciseMapStructMapper INSTANCE = getMapper(ExerciseMapStructMapper.class);

    /**
     * GetPageListPayload -> Exercise.
     *
     * @param payload the payload
     * @return the exercise
     */
    Exercise of(GetPageListPayload payload);
}
