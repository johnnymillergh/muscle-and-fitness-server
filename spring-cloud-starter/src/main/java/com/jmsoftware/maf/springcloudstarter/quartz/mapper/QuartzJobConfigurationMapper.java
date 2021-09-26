package com.jmsoftware.maf.springcloudstarter.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: QuartzJobConfigurationMapper
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 8:23 AM
 */
@Mapper
public interface QuartzJobConfigurationMapper extends BaseMapper<QuartzJobConfiguration> {
}
