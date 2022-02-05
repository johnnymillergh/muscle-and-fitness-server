package com.jmsoftware.maf.springcloudstarter.quartz.converter;

import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.factory.Mappers.getMapper;

/**
 * Description: QuartzJobConfigurationMapStructMapper, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/5/2022 2:44 PM
 **/
@Mapper
public interface QuartzJobConfigurationMapStructMapper {
    QuartzJobConfigurationMapStructMapper INSTANCE = getMapper(QuartzJobConfigurationMapStructMapper.class);

    /**
     * Convert from quartz job configuration.
     *
     * @param quartzJobConfigurationExcel the quartz job configuration excel
     * @return the quartz job configuration
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    QuartzJobConfiguration of(QuartzJobConfigurationExcel quartzJobConfigurationExcel);
}
