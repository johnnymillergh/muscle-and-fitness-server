package com.jmsoftware.maf.springcloudstarter.quartz.controller

import cn.hutool.core.text.CharSequenceUtil
import com.jmsoftware.maf.common.bean.PageResponseBodyBean
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.util.LoggerDelegate
import com.jmsoftware.maf.springcloudstarter.poi.AbstractExcelDataController
import com.jmsoftware.maf.springcloudstarter.quartz.entity.CreateOrModifyQuartzJobConfigurationPayload
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload
import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobConfigurationService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.Instant
import javax.validation.Valid

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
    ): PageResponseBodyBean<GetQuartzJobConfigurationPageListItem> {
        return service.getPageList(payload)
    }

    @PostMapping("/configurations")
    fun create(
        @RequestBody payload: @Valid CreateOrModifyQuartzJobConfigurationPayload
    ): ResponseBodyBean<Long> {
        return ResponseBodyBean.ofSuccess(service.create(payload))
    }

    @PutMapping("/configurations/{id}")
    fun modify(
        @PathVariable id: Long,
        @Valid @RequestBody payload: CreateOrModifyQuartzJobConfigurationPayload
    ): ResponseBodyBean<Long> {
        return ResponseBodyBean.ofSuccess(service.modify(id, payload))
    }

    @PatchMapping("/configurations/{id}/{property}")
    fun patch(
        @PathVariable id: Long,
        @PathVariable property: String,
        @RequestBody payload: CreateOrModifyQuartzJobConfigurationPayload
    ): ResponseBodyBean<Long> {
        return ResponseBodyBean.ofSuccess(service.patch(id, property, payload))
    }

    @PostMapping("/configurations/{id}/run-immediately")
    fun runImmediately(@PathVariable id: Long): ResponseBodyBean<Long> {
        return ResponseBodyBean.ofSuccess(service.runImmediately(id))
    }

    @DeleteMapping("/configurations/{id}/{group}")
    fun delete(@PathVariable id: Long, @PathVariable group: String): ResponseBodyBean<Long> {
        return ResponseBodyBean.ofSuccess(service.delete(id, group))
    }

    override fun onExceptionOccurred() {
        log.error("Exception occurred when uploading excel. Excel class: `${QuartzJobConfigurationExcel::class.java}`")
        fileName.set(CharSequenceUtil.format("quartz-job-configuration-stat-{}.xlsx", Instant.now()))
    }

    override fun validateBeforeAddToBeanList(
        beanList: List<QuartzJobConfigurationExcel>,
        bean: QuartzJobConfigurationExcel, index: Int
    ) {
        service.validateBeforeAddToBeanList(beanList, bean, index)
    }

    override fun executeDatabaseOperation(beanList: List<QuartzJobConfigurationExcel>) {
        service.save(beanList)
    }

    override fun getTemplateFileName(): String {
        return QuartzJobConfigurationService.TEMPLATE_EXCEL
    }

    override fun getListForExporting(): List<QuartzJobConfigurationExcel> {
        return service.getListForExporting()
    }
}
