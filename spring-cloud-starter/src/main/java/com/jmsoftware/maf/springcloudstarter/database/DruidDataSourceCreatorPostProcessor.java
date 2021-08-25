package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.dynamic.datasource.creator.DruidDataSourceCreator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Description: DruidDataSourceCreatorPostProcessor, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/25/2021 11:27 AM
 **/
@Slf4j
public class DruidDataSourceCreatorPostProcessor implements BeanPostProcessor {
    @Nullable
    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof DruidDataSourceCreator) {
            this.postProcessDynamicDataSourceProperties((DruidDataSourceCreator) bean);
        }
        return bean;
    }

    private void postProcessDynamicDataSourceProperties(DruidDataSourceCreator bean) {
        val cpuCoreCount = Runtime.getRuntime().availableProcessors();
        val druidConfig = bean.getGConfig();
        log.warn("Setting Druid connection pool by current cpuCoreCount: {}", cpuCoreCount);
        druidConfig.setInitialSize(cpuCoreCount);
        druidConfig.setMaxActive(cpuCoreCount * 2 + 1);
    }
}
