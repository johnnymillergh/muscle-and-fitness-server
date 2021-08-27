package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.dynamic.datasource.creator.DruidDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * <p>DruidDataSourceCreatorPostProcessor is to enhance Druid connection pool by CPU core count.</p>
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
public class DruidDataSourceCreatorPostProcessor implements BeanPostProcessor, DisposableBean {
    private final ApplicationContext applicationContext;
    private final DynamicDataSourceProperties dynamicDataSourceProperties;
    private int initializedBeanCounter = 0;

    @Nullable
    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        this.initializedBeanCounter++;
        if (bean instanceof DruidDataSourceCreator) {
            this.enhanceConnectionPoolSize();
        }
        return bean;
    }

    /**
     * Post process dynamic data source properties. Enhance connection pool size by available processor count
     * (logical processor count)
     *
     * <p>A formula which has held up pretty well across a lot of benchmarks for years is that for optimal throughput
     * the number of active connections should be somewhere near ((<em>core_count</em> * 2) +
     * <em>effective_spindle_count</em>). Core count should not include HT threads, even if hyperthreading is enabled
     * . Effective spindle count is zero if the active data set is fully cached, and approaches the actual number of
     * spindles as the cache hit rate falls. Benchmarks of WIP for version 9.2 suggest that this formula will need
     * adjustment on that release. There hasn&#39;t been any analysis so far regarding how well the formula works
     * with SSDs.</p>
     * <p>However you choose a starting point for a connection pool size, you should probably try incremental
     * adjustments with your production system to find the actual &quot;sweet spot&quot; for your hardware and
     * workload.</p>
     * <p>Remember that this &quot;sweet spot&quot; is for the number of connections that are <em>actively doing
     * work</em>. Ignore mostly-idle connections used for system monitoring and control when working out an
     * appropriate pool size. You should always make <strong>max_connections</strong> a bit bigger than the number of
     * connections you enable in your connection pool. That way there are always a few slots available for direct
     * connections for system maintenance and monitoring.</p>
     *
     * @see
     * <a href='https://wiki.postgresql.org/wiki/Number_Of_Database_Connections#How_to_Find_the_Optimal_Database_Connection_Pool_Size'>How to Find the Optimal Database Connection Pool Size</a>
     * @see
     * <a href='https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-usagenotes-j2ee-concepts-connection-pooling.html#idm46216069663472'>Sizing the Connection Pool</a>
     */
    private void enhanceConnectionPoolSize() {
        log.info("Spring initialized {} beans previously, beanDefinitionCount: {}", this.initializedBeanCounter,
                 this.applicationContext.getBeanDefinitionCount());
        val cpuCoreCount = Runtime.getRuntime().availableProcessors();
        val minConnectionPoolSize = cpuCoreCount * 2 + 1;
        val maxConnectionPoolSize = cpuCoreCount * 3;
        this.dynamicDataSourceProperties.getDruid()
                .setInitialSize(minConnectionPoolSize)
                .setMinIdle(minConnectionPoolSize)
                .setMaxActive(maxConnectionPoolSize);
        log.warn("Druid connection pool enhanced by current cpuCoreCount: {}, initial size: {}, min idle: {}" +
                         ", max active: {}",
                 cpuCoreCount, minConnectionPoolSize, minConnectionPoolSize, maxConnectionPoolSize);
        val defaultListableBeanFactory =
                (DefaultListableBeanFactory) this.applicationContext.getAutowireCapableBeanFactory();
        defaultListableBeanFactory.destroyBean(this);
    }

    @Override
    public void destroy() {
        log.warn("Destroyed bean {}", this.getClass().getSimpleName());
    }
}
