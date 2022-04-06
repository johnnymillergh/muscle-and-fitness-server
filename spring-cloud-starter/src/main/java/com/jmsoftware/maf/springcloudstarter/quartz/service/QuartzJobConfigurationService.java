package com.jmsoftware.maf.springcloudstarter.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.CreateOrModifyQuartzJobConfigurationPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Description: QuartzJobConfigurationService
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 8:23 AM
 **/
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
    void validateBeforeAddToBeanList(
            List<QuartzJobConfigurationExcel> beanList,
            QuartzJobConfigurationExcel bean,
            int index
    );

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

    /**
     * Create create quartz job configuration response.
     *
     * @param payload the payload
     * @return the quartz job configuration id
     */
    Long create(@Valid @NotNull CreateOrModifyQuartzJobConfigurationPayload payload);

    /**
     * Modify modify quartz job configuration response.
     *
     * @param id      the id
     * @param payload the payload
     * @return the quartz job configuration id
     */
    Long modify(
            @NotNull Long id,
            @Valid @NotNull CreateOrModifyQuartzJobConfigurationPayload payload
    );

    /**
     * Patch create or modify quartz job configuration response.
     *
     * @param id       the id
     * @param property the property
     * @param payload  the payload
     * @return the quartz job configuration id
     */
    Long patch(
            @NotNull Long id,
            @NotBlank String property,
            @NotNull CreateOrModifyQuartzJobConfigurationPayload payload
    );

    /**
     * Run immediately.
     *
     * @param id the id
     * @return the quartz job configuration id
     */
    Long runImmediately(@NotNull Long id);

    /**
     * Delete quartz job long.
     *
     * @param id    the id
     * @param group the group
     * @return the long
     */
    Long delete(@NotNull Long id, @NotBlank String group);
}
