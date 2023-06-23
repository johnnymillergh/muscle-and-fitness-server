package com.jmsoftware.maf.springcloudstarter.quartz.service

import com.baomidou.mybatisplus.extension.service.IService
import com.jmsoftware.maf.common.bean.PageResponseBodyBean
import com.jmsoftware.maf.springcloudstarter.quartz.entity.CreateOrModifyQuartzJobConfigurationPayload
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload
import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

/**
 * Description: QuartzJobConfigurationService
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 8:23 AM
 */
@Validated
interface QuartzJobConfigurationService : IService<QuartzJobConfiguration> {
    companion object {
        const val TEMPLATE_EXCEL = "quartz-job-configuration-stat.xlsx"
    }

    /**
     * Gets page list.
     *
     * @param payload the payload
     * @return the page list
     */
    fun getPageList(
        payload: @Valid @NotNull GetQuartzJobConfigurationPageListPayload
    ): PageResponseBodyBean<GetQuartzJobConfigurationPageListItem>

    /**
     * Validate before add to bean list.
     *
     * @param beanList the bean list
     * @param bean     the bean
     * @param index    the index
     */
    fun validateBeforeAddToBeanList(
        beanList: List<QuartzJobConfigurationExcel>,
        bean: QuartzJobConfigurationExcel,
        index: Int
    )

    /**
     * Save.
     *
     * @param beanList the bean list
     */
    fun save(beanList: @NotEmpty List<QuartzJobConfigurationExcel>)

    /**
     * Gets list for exporting.
     *
     * @return the list for exporting
     */
    fun getListForExporting(): List<QuartzJobConfigurationExcel>

    /**
     * Create create quartz job configuration response.
     *
     * @param payload the payload
     * @return the quartz job configuration id
     */
    fun create(payload: @Valid @NotNull CreateOrModifyQuartzJobConfigurationPayload): Long

    /**
     * Modify modify quartz job configuration response.
     *
     * @param id      the id
     * @param payload the payload
     * @return the quartz job configuration id
     */
    fun modify(
        id: @NotNull Long,
        payload: @Valid @NotNull CreateOrModifyQuartzJobConfigurationPayload
    ): Long

    /**
     * Patch create or modify quartz job configuration response.
     *
     * @param id       the id
     * @param property the property
     * @param payload  the payload
     * @return the quartz job configuration id
     */
    fun patch(
        id: @NotNull Long,
        property: @NotBlank String,
        payload: @NotNull CreateOrModifyQuartzJobConfigurationPayload
    ): Long

    /**
     * Run immediately.
     *
     * @param id the id
     * @return the quartz job configuration id
     */
    fun runImmediately(id: @NotNull Long): Long

    /**
     * Delete quartz job long.
     *
     * @param id    the id
     * @param group the group
     * @return the long
     */
    fun delete(id: @NotNull Long, group: @NotBlank String): Long
}
