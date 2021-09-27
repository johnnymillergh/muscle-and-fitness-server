package com.jmsoftware.maf.springcloudstarter.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Description: QuartzJobConfigurationService
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 8:23 AM
 */
@Validated
public interface QuartzJobConfigurationService extends IService<QuartzJobConfiguration> {
    String TEMPLATE_EXCEL = "quartz-job-configuration-stat.xlsx";

    /**
     * Gets page list.
     *
     * @param payload the payload
     * @return the page list
     */
    PageResponseBodyBean<GetQuartzJobConfigurationPageListItem> getPageList(
            @Valid GetQuartzJobConfigurationPageListPayload payload
    );

    /**
     * Validate before add to bean list.
     *
     * @param beanList the bean list
     * @param bean     the bean
     * @param index    the index
     */
    void validateBeforeAddToBeanList(List<QuartzJobConfigurationExcel> beanList, QuartzJobConfigurationExcel bean,
                                     int index);

    /**
     * Save.
     *
     * @param beanList the bean list
     */
    void save(@NotEmpty List<@Valid QuartzJobConfigurationExcel> beanList);

    /**
     * Gets list for exporting.
     *
     * @return the list for exporting
     */
    List<QuartzJobConfigurationExcel> getListForExporting();
}
