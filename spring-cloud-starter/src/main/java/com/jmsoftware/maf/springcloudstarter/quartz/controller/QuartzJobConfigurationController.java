package com.jmsoftware.maf.springcloudstarter.quartz.controller;

import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.poi.AbstractExcelDataController;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.CreateOrModifyQuartzJobConfigurationPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel;
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

import static cn.hutool.core.text.CharSequenceUtil.format;

/**
 * <h1>QuartzJobConfigurationController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/26/21 11:58 PM
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/quartz-job")
public class QuartzJobConfigurationController extends AbstractExcelDataController<QuartzJobConfigurationExcel> {
    private final QuartzJobConfigurationService service;

    @GetMapping("/quartz-job-configurations")
    public PageResponseBodyBean<GetQuartzJobConfigurationPageListItem> getPageList(
            @Valid GetQuartzJobConfigurationPageListPayload payload
    ) {
        return this.service.getPageList(payload);
    }

    @PostMapping("/quartz-job-configurations")
    public ResponseBodyBean<Long> create(
            @Valid @RequestBody CreateOrModifyQuartzJobConfigurationPayload payload
    ) {
        return ResponseBodyBean.ofSuccess(this.service.create(payload));
    }

    @PutMapping("/quartz-job-configurations/{id}")
    public ResponseBodyBean<Long> modify(
            @PathVariable Long id,
            @Valid @RequestBody CreateOrModifyQuartzJobConfigurationPayload payload
    ) {
        return ResponseBodyBean.ofSuccess(this.service.modify(id, payload));
    }

    @PatchMapping("/quartz-job-configurations/{id}/{property}")
    public ResponseBodyBean<Long> patch(
            @PathVariable Long id,
            @PathVariable String property,
            @RequestBody CreateOrModifyQuartzJobConfigurationPayload payload
    ) {
        return ResponseBodyBean.ofSuccess(this.service.patch(id, property, payload));
    }

    @PostMapping("/quartz-job-configurations/{id}/run-immediately")
    public ResponseBodyBean<Long> runImmediately(@PathVariable Long id) {
        return ResponseBodyBean.ofSuccess(this.service.runImmediately(id));
    }

    @DeleteMapping("/quartz-job-configurations/{id}/{group}")
    public ResponseBodyBean<Long> delete(@PathVariable Long id, @PathVariable String group) {
        return ResponseBodyBean.ofSuccess(this.service.delete(id, group));
    }

    @Override
    protected void onExceptionOccurred() {
        log.error("Exception occurred when uploading excel. Excel class: {}", QuartzJobConfigurationExcel.class);
        this.fileName.set(format("quartz-job-configuration-stat-{}.xlsx", Instant.now()));
    }

    @Override
    protected void validateBeforeAddToBeanList(List<QuartzJobConfigurationExcel> beanList,
                                               QuartzJobConfigurationExcel bean, int index)
            throws IllegalArgumentException {
        this.service.validateBeforeAddToBeanList(beanList, bean, index);
    }

    @Override
    protected void executeDatabaseOperation(List<QuartzJobConfigurationExcel> beanList) {
        this.service.save(beanList);
    }

    @Override
    protected String getTemplateFileName() {
        return QuartzJobConfigurationService.TEMPLATE_EXCEL;
    }

    @Override
    protected List<QuartzJobConfigurationExcel> getListForExporting() {
        return this.service.getListForExporting();
    }
}
