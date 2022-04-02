package com.jmsoftware.maf.springcloudstarter.quartz.job;

import cn.hutool.core.bean.BeanUtil;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import static com.jmsoftware.maf.springcloudstarter.quartz.constant.ScheduleConstant.QUARTZ_JOB_CONFIGURATION;

/**
 * AbstractQuartzJob
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/24/2021 3:51 PM
 */
@Slf4j
abstract class AbstractQuartzJob extends QuartzJobBean {
    private static final String QUARTZ_JOB_CONFIGURATION_CLASS = QuartzJobConfiguration.class.getName();

    @Override
    protected final void executeInternal(JobExecutionContext context) {
        val sourceQuartzJobConfiguration = context.getMergedJobDataMap().get(QUARTZ_JOB_CONFIGURATION);
        if (!(QUARTZ_JOB_CONFIGURATION_CLASS.equals(sourceQuartzJobConfiguration.getClass().getName()))) {
            throw new IllegalArgumentException(
                    "Invalid job data! Not the instance of QuartzJobConfiguration. Runtime actual class: "
                            + sourceQuartzJobConfiguration.getClass());
        }
        if (log.isDebugEnabled()) {
            log.debug("Found and QuartzJobConfiguration from job data map: {}", sourceQuartzJobConfiguration);
        }
        val quartzJobConfiguration = new QuartzJobConfiguration();
        BeanUtil.copyProperties(sourceQuartzJobConfiguration, quartzJobConfiguration);
        try {
            this.invoke(context, quartzJobConfiguration);
        } catch (Exception e) {
            log.error("Exception occurred when invoking method", e);
        }
    }

    /**
     * Invoke.
     *
     * @param context                the context
     * @param quartzJobConfiguration the quartz job configuration
     */
    protected abstract void invoke(JobExecutionContext context, QuartzJobConfiguration quartzJobConfiguration);
}
