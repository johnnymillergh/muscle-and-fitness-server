package com.jmsoftware.maf.springcloudstarter.quartz.controller;

import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>QuartzJobConfigurationController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/26/21 11:58 PM
 **/
@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = {"Quartz Job Configuration API"})
public class QuartzJobConfigurationController {
    private final QuartzJobConfigurationService service;

    @GetMapping("/quartz-job-configurations")
    @ApiOperation(value = "/quartz-job-configurations", notes = "Retrieve Quartz job configuration page list")
    public PageResponseBodyBean<GetQuartzJobConfigurationPageListItem> getPageList(
            @Valid GetQuartzJobConfigurationPageListPayload payload
    ) {
        return this.service.getPageList(payload);
    }
}
