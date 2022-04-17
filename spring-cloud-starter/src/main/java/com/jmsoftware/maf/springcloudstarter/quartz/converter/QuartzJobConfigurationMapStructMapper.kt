package com.jmsoftware.maf.springcloudstarter.quartz.converter

import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers.getMapper

/**
 * # QuartzJobConfigurationMapStructMapper
 *
 * Description: QuartzJobConfigurationMapStructMapper, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:34 PM
 */
@Mapper
interface QuartzJobConfigurationMapStructMapper {
    companion object {
        val INSTANCE: QuartzJobConfigurationMapStructMapper =
            getMapper(QuartzJobConfigurationMapStructMapper::class.java)
    }

    /**
     * Convert from quartz job configuration.
     *
     * @param quartzJobConfigurationExcel the quartz job configuration excel
     * @return the quartz job configuration
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    fun of(quartzJobConfigurationExcel: QuartzJobConfigurationExcel): QuartzJobConfiguration
}
