package com.jmsoftware.maf.springcloudstarter.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import org.springframework.validation.annotation.Validated;

/**
 * Description: QuartzJobConfigurationService
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 8:23 AM
 */
@Validated
public interface QuartzJobConfigurationService extends IService<QuartzJobConfiguration> {
}
