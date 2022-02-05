package com.jmsoftware.maf.mafmis.exercise.converter;

import com.jmsoftware.maf.mafmis.exercise.payload.GetPageListPayload;
import com.jmsoftware.maf.mafmis.exercise.persistence.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    @Mapping(target = "preparation", ignore = true)
    @Mapping(target = "execution", ignore = true)
    @Mapping(target = "exerciseGifPath", ignore = true)
    Exercise of(GetPageListPayload payload);
}
