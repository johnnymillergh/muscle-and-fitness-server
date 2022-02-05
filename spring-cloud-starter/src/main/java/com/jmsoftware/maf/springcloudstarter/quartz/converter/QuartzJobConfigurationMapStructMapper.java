/*
 * Copyright By ZATI
 * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
 * All rights reserved.
 *
 * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or
 * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your
 * system.
 */
package com.jmsoftware.maf.springcloudstarter.quartz.converter;

import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Description: QuartzJobConfigurationMapStructMapper, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/5/2022 2:44 PM
 **/
@Mapper
public interface QuartzJobConfigurationMapStructMapper {
    QuartzJobConfigurationMapStructMapper INSTANCE = Mappers.getMapper(QuartzJobConfigurationMapStructMapper.class);

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
