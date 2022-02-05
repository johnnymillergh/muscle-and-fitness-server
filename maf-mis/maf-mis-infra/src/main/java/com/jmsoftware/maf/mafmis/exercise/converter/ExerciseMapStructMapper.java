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
