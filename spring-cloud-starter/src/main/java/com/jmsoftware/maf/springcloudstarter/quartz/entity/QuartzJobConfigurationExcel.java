package com.jmsoftware.maf.springcloudstarter.quartz.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Description: QuartzJobConfigurationExcel, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/27/2021 11:26 AM
 **/
@Data
public class QuartzJobConfigurationExcel {
    @NotBlank
    private String name;
    private String group;
    private String serviceName;
    @NotBlank
    private String invokeTarget;
    @NotBlank
    private String cronExpression;
    private Byte misfirePolicy;
    private Byte concurrent;
    @NotBlank
    private String description;
    private Byte status;
    private String createdBy;
    private LocalDateTime createdTime;
    private String modifiedBy;
    private LocalDateTime modifiedTime;
}
