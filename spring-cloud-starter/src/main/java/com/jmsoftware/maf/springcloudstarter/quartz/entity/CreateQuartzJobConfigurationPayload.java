package com.jmsoftware.maf.springcloudstarter.quartz.entity;

import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import lombok.Data;
import lombok.val;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <h1>CreateQuartzJobConfigurationPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 10/1/21 10:29 PM
 **/
@Data
public class CreateQuartzJobConfigurationPayload {
    @NotBlank
    @Length(max = 50)
    private String name;
    @NotBlank
    @Length(max = 200)
    private String group;
    @NotBlank
    @Length(max = 500)
    private String invokeTarget;
    @NotBlank
    @Length(max = 50)
    private String cronExpression;
    @NotNull
    private Byte misfirePolicy;
    @NotNull
    private Byte concurrent;
    @NotBlank
    @Length(max = 1000)
    private String description;
    @NotNull
    private Byte status;

    public QuartzJobConfiguration asQuartzJobConfiguration() {
        val quartzJobConfiguration = new QuartzJobConfiguration();
        quartzJobConfiguration.setName(this.name);
        quartzJobConfiguration.setGroup(this.group);
        quartzJobConfiguration.setInvokeTarget(this.invokeTarget);
        quartzJobConfiguration.setCronExpression(this.cronExpression);
        quartzJobConfiguration.setMisfirePolicy(this.misfirePolicy);
        quartzJobConfiguration.setConcurrent(this.concurrent);
        quartzJobConfiguration.setDescription(this.description);
        quartzJobConfiguration.setStatus(this.status);
        return quartzJobConfiguration;
    }
}
