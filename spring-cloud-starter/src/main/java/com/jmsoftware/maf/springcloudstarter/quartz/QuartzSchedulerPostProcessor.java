package com.jmsoftware.maf.springcloudstarter.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * <p>QuartzSchedulerPostProcessor is to enhance Quartz thread pool by CPU core count.</p>
 * <p><a href='https://en.wikipedia.org/wiki/Hyper-threading#Overview'>Hyper-threading</a>
 * works by duplicating certain sections of the processor—those that store the
 * <a href='https://en.wikipedia.org/wiki/Architectural_state'>architectural state</a>
 * —but not duplicating the main <a href='https://en.wikipedia.org/wiki/Execution_unit'>execution resources</a>
 * . This allows a hyper-threading processor to appear as the usual &quot;physical&quot; processor and an extra
 * &quot;<a href='https://en.wikipedia.org/wiki/Virtualization'>logical</a>
 * &quot; processor to the host operating system (HTT-unaware operating systems see two &quot;physical&quot;
 * processors), allowing the operating system to schedule two threads or processes simultaneously and appropriately.</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/25/2021 11:27 AM
 **/
@Slf4j
@RequiredArgsConstructor
public class QuartzSchedulerPostProcessor implements BeanPostProcessor, DisposableBean {
    private static final String THREAD_COUNT = "org.quartz.threadPool.threadCount";
    private final ApplicationContext applicationContext;
    private final QuartzProperties quartzProperties;
    private int initializedBeanCounter = 0;

    @Nullable
    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        this.initializedBeanCounter++;
        if (bean instanceof SchedulerFactoryBean) {
            this.enhanceThreadPool();
        }
        return bean;
    }

    private void enhanceThreadPool() {
        log.info("Spring initialized {} beans previously, beanDefinitionCount: {}", this.initializedBeanCounter,
                 this.applicationContext.getBeanDefinitionCount());
        val cpuCoreCount = Runtime.getRuntime().availableProcessors();
        val threadCount = String.valueOf(cpuCoreCount * 2 + 1);
        this.quartzProperties.getProperties().put(THREAD_COUNT, threadCount);
        log.warn("Quartz thread pool enhanced by current cpuCoreCount: {}, threadCount: {}", cpuCoreCount,
                 threadCount);
        this.applicationContext.getAutowireCapableBeanFactory().destroyBean(this);
    }

    @Override
    public void destroy() {
        log.warn("Destroyed bean {}", this.getClass().getSimpleName());
    }
}
