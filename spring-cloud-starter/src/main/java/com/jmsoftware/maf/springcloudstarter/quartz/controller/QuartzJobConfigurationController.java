package com.jmsoftware.maf.springcloudstarter.quartz.controller;

import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.poi.AbstractExcelDataController;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.CreateOrModifyQuartzJobConfigurationPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel;
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

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
@Api(tags = {"Quartz Job Configuration API"})
public class QuartzJobConfigurationController extends AbstractExcelDataController<QuartzJobConfigurationExcel> {
    private final QuartzJobConfigurationService service;

    @GetMapping("/quartz-job-configurations")
    @ApiOperation(value = "Retrieve Quartz job configuration", notes = "Retrieve Quartz job configuration page list")
    public PageResponseBodyBean<GetQuartzJobConfigurationPageListItem> getPageList(
            @Valid GetQuartzJobConfigurationPageListPayload payload
    ) {
        return this.service.getPageList(payload);
    }

    @PostMapping("/quartz-job-configurations")
    @ApiOperation(value = "Create Quartz job configuration", notes = "Create Quartz job configuration")
    public ResponseBodyBean<Long> create(
            @Valid @RequestBody CreateOrModifyQuartzJobConfigurationPayload payload
    ) {
        return ResponseBodyBean.ofSuccess(this.service.create(payload));
    }

    @PutMapping("/quartz-job-configurations/{id}")
    @ApiOperation(value = "Modify Quartz job configuration", notes = "Modify Quartz job configuration")
    public ResponseBodyBean<Long> modify(
            @PathVariable Long id,
            @Valid @RequestBody CreateOrModifyQuartzJobConfigurationPayload payload
    ) {
        return ResponseBodyBean.ofSuccess(this.service.modify(id, payload));
    }

    @PatchMapping("/quartz-job-configurations/{id}/{property}")
    @ApiOperation(value = "Patch Quartz job configuration", notes = "Patch Quartz job configuration")
    public ResponseBodyBean<Long> patch(
            @PathVariable Long id,
            @PathVariable String property,
            @RequestBody CreateOrModifyQuartzJobConfigurationPayload payload
    ) {
        return ResponseBodyBean.ofSuccess(this.service.patch(id, property, payload));
    }

    @PostMapping("/quartz-job-configurations/{id}/run-immediately")
    @ApiOperation(value = "Run Quartz job immediately", notes = "Run Quartz job immediately")
    public ResponseBodyBean<Long> runImmediately(@PathVariable Long id) {
        return ResponseBodyBean.ofSuccess(this.service.runImmediately(id));
    }

    @DeleteMapping("/quartz-job-configurations/{id}/{group}")
    @ApiOperation(value = "Delete Quartz job configuration", notes = "Delete Quartz job configuration")
    public ResponseBodyBean<Long> delete(@PathVariable Long id, @PathVariable String group) {
        return ResponseBodyBean.ofSuccess(this.service.delete(id, group));
    }

    @Override
    protected void onExceptionOccurred() {
        log.error("Exception occurred when uploading excel. Excel class: {}", QuartzJobConfigurationExcel.class);
        this.fileName.set(String.format("quartz-job-configuration-stat-%s.xlsx", Instant.now()));
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
