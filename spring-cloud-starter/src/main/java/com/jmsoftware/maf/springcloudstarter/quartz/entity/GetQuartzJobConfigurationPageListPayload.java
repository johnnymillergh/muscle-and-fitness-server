package com.jmsoftware.maf.springcloudstarter.quartz.entity;

import com.jmsoftware.maf.common.bean.PaginationBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h1>GetQuartzJobConfigurationPageListPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/27/21 12:07 AM
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class GetQuartzJobConfigurationPageListPayload extends PaginationBase {
    private String serviceName;
}
