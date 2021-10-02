package com.jmsoftware.maf.springcloudstarter.quartz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <h1>GetQuartzJobConfigurationPageListItem</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/27/21 12:07 AM
 **/
@Data
public class GetQuartzJobConfigurationPageListItem {
    private Long id;
    private String name;
    private String group;
    private String serviceName;
    private String invokeTarget;
    private String cronExpression;
    private Byte misfirePolicy;
    private Byte concurrent;
    private String description;
    private Byte status;
    private String createdBy;
    private LocalDateTime createdTime;
    private String modifiedBy;
    private LocalDateTime modifiedTime;
}
