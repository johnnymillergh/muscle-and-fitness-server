package com.jmsoftware.maf.springcloudstarter.helper;

import cn.hutool.core.lang.Console;
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;

import java.time.Instant;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Description: SpringBootStartupHelper, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 2/10/2021 10:07 AM
 **/
@Slf4j
@RequiredArgsConstructor
public class SpringBootStartupHelper implements DisposableBean {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private final MafProjectProperties mafProjectProperties;
    private final IpHelper ipHelper;
    private final ApplicationContext applicationContext;

    public void stop(@NonNull StopWatch stopWatch) {
        stopWatch.stop();
        Console.log("🥳 Congratulations! 🎉");
        Console.log("🖥 {}@{} started!", this.mafProjectProperties.getProjectArtifactId(),
                 this.mafProjectProperties.getVersion());
        Console.log("⚙️ Environment: {}", this.mafProjectProperties.getEnvironment());
        Console.log("⏳ Deployment duration: {} seconds ({} ms)", stopWatch.getTotalTimeSeconds(),
                 stopWatch.getTotalTimeMillis());
        Console.log("⏰ App started at {} (timezone - {})", Instant.now().atZone(ZoneId.of("UTC+8")),
                 TimeZone.getDefault().getDisplayName());
        Console.error("  App running at{}  - Local:   http://localhost:{}{}/{}  - Network: http://{}:{}{}/",
                      LINE_SEPARATOR, this.ipHelper.getServerPort(), this.mafProjectProperties.getContextPath(),
                      LINE_SEPARATOR, this.ipHelper.getPublicIp(), this.ipHelper.getServerPort(),
                      this.mafProjectProperties.getContextPath());
        val defaultListableBeanFactory =
                (DefaultListableBeanFactory) this.applicationContext.getAutowireCapableBeanFactory();
        defaultListableBeanFactory.destroyBean(this);
    }

    @Override
    public void destroy() {
        log.warn("Destroyed bean: {}", this.getClass().getSimpleName());
    }
}
