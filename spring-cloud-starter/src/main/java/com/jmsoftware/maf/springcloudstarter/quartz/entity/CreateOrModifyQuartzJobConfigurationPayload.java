package com.jmsoftware.maf.springcloudstarter.quartz.entity;

import com.jmsoftware.maf.springcloudstarter.quartz.constant.Concurrent;
import com.jmsoftware.maf.springcloudstarter.quartz.constant.MisfirePolicy;
import com.jmsoftware.maf.springcloudstarter.quartz.constant.QuartzJobStatus;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import com.jmsoftware.maf.springcloudstarter.validation.annotation.ValidEnumValue;
import lombok.Data;
import lombok.val;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <h1>CreateOrModifyQuartzJobConfigurationPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 10/1/21 11:43 PM
 **/
@Data
public class CreateOrModifyQuartzJobConfigurationPayload {
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
    @ValidEnumValue(targetEnum = MisfirePolicy.class)
    private Byte misfirePolicy;
    @NotNull
    @ValidEnumValue(targetEnum = Concurrent.class)
    private Byte concurrent;
    @NotBlank
    @Length(max = 1000)
    private String description;
    @NotNull
    @ValidEnumValue(targetEnum = QuartzJobStatus.class)
    private Byte status;

    public QuartzJobConfiguration asQuartzJobConfiguration() {
        val result = new QuartzJobConfiguration();
        result.setName(this.name);
        result.setGroup(this.group);
        result.setInvokeTarget(this.invokeTarget);
        result.setCronExpression(this.cronExpression);
        result.setMisfirePolicy(this.misfirePolicy);
        result.setConcurrent(this.concurrent);
        result.setDescription(this.description);
        result.setStatus(this.status);
        return result;
    }
}
