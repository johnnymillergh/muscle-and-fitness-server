package com.jmsoftware.maf.springcloudstarter.quartz.controller

import com.jmsoftware.maf.common.bean.PageResponseBodyBean
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.util.LoggerDelegate
import com.jmsoftware.maf.springcloudstarter.poi.AbstractExcelDataController
import com.jmsoftware.maf.springcloudstarter.quartz.entity.CreateOrModifyQuartzJobConfigurationPayload
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload
import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobConfigurationService
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.Instant

/**
 * # QuartzJobConfigurationController
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 9:12 PM
 */
@Validated
@RestController
@RequestMapping("/quartz-job")
class QuartzJobConfigurationController(
    private val service: QuartzJobConfigurationService
) : AbstractExcelDataController<QuartzJobConfigurationExcel>() {
    companion object {
        private val log by LoggerDelegate()
    }

    @GetMapping("/configurations")
    fun getPageList(
        payload: @Valid GetQuartzJobConfigurationPageListPayload
    ): PageResponseBodyBean<GetQuartzJobConfigurationPageListItem> = service.getPageList(payload)

    @PostMapping("/configurations")
    fun create(
        @RequestBody payload: @Valid CreateOrModifyQuartzJobConfigurationPayload
    ): ResponseBodyBean<Long> = ResponseBodyBean.ofSuccess(service.create(payload))

    @PutMapping("/configurations/{id}")
    fun modify(
        @PathVariable id: Long,
        @Valid @RequestBody payload: CreateOrModifyQuartzJobConfigurationPayload
    ): ResponseBodyBean<Long> = ResponseBodyBean.ofSuccess(service.modify(id, payload))

    @PatchMapping("/configurations/{id}/{property}")
    fun patch(
        @PathVariable id: Long,
        @PathVariable property: String,
        @RequestBody payload: CreateOrModifyQuartzJobConfigurationPayload
    ): ResponseBodyBean<Long> = ResponseBodyBean.ofSuccess(service.patch(id, property, payload))

    @PostMapping("/configurations/{id}/run-immediately")
    fun runImmediately(@PathVariable id: Long): ResponseBodyBean<Long> =
        ResponseBodyBean.ofSuccess(service.runImmediately(id))

    @DeleteMapping("/configurations/{id}/{group}")
    fun delete(@PathVariable id: Long, @PathVariable group: String): ResponseBodyBean<Long> =
        ResponseBodyBean.ofSuccess(service.delete(id, group))

    override fun onExceptionOccurred() {
        log.error("Exception occurred when uploading excel. Excel class: `${QuartzJobConfigurationExcel::class.java}`")
        fileName.set("quartz-job-configuration-stat-${Instant.now()}.xlsx")
    }

    override fun validateBeforeAddToBeanList(
        beanList: List<QuartzJobConfigurationExcel>,
        bean: QuartzJobConfigurationExcel, index: Int
    ) = service.validateBeforeAddToBeanList(beanList, bean, index)

    override fun executeDatabaseOperation(beanList: List<QuartzJobConfigurationExcel>) = service.save(beanList)

    override fun getTemplateFileName(): String = QuartzJobConfigurationService.TEMPLATE_EXCEL

    override fun getListForExporting(): List<QuartzJobConfigurationExcel> = service.getListForExporting()
}
