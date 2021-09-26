package com.jmsoftware.maf.springcloudstarter.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * Description: QuartzJobConfigurationService
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 8:23 AM
 */
@Validated
public interface QuartzJobConfigurationService extends IService<QuartzJobConfiguration> {
    /**
     * Gets page list.
     *
     * @param payload the payload
     * @return the page list
     */
    PageResponseBodyBean<GetQuartzJobConfigurationPageListItem> getPageList(
            @Valid GetQuartzJobConfigurationPageListPayload payload
    );
}
