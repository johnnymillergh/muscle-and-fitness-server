package com.jmsoftware.maf.springcloudstarter.quartz.job;

import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import com.jmsoftware.maf.springcloudstarter.quartz.util.QuartzJobInvocationUtil;
import org.quartz.JobExecutionContext;

/**
 * QuartzJobExecution. Concurrent execution
 *
 * @author 钟俊 (zhongjun), email: zhongjun@toguide.cn, date: 5/12/2021 2:11 PM
 */
public final class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void invoke(JobExecutionContext context, QuartzJobConfiguration quartzJobConfiguration) {
        QuartzJobInvocationUtil.invokeMethod(quartzJobConfiguration);
    }
}
