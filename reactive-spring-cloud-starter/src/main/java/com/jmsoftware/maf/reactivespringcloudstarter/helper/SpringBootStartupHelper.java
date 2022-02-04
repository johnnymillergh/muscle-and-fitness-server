package com.jmsoftware.maf.reactivespringcloudstarter.helper;

import cn.hutool.core.lang.Console;
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafProjectProperties;
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
    @SuppressWarnings("HttpUrlsUsage")
    private static final String TEMPLATE = LINE_SEPARATOR +
            "🥳 Congratulations! 🎉" + LINE_SEPARATOR +
            "🖥 {}@{} started! " + LINE_SEPARATOR +
            "⚙️ Environment: {}" + LINE_SEPARATOR +
            "⏳ Deployment duration: {} seconds ({} ms)" + LINE_SEPARATOR +
            "⏰ App started at {} (timezone - {})" + LINE_SEPARATOR +
            "  App running at" + LINE_SEPARATOR +
            "  - Local:   http://localhost:{}{}/" + LINE_SEPARATOR +
            "  - Network: http://{}:{}{}/";
    private final MafProjectProperties mafProjectProperties;
    private final IpHelper ipHelper;
    private final ApplicationContext applicationContext;

    public void stop(@NonNull StopWatch stopWatch) {
        stopWatch.stop();
        log.info(
                TEMPLATE,
                this.mafProjectProperties.getProjectArtifactId(), this.mafProjectProperties.getVersion(),
                this.mafProjectProperties.getEnvironment(),
                stopWatch.getTotalTimeSeconds(), stopWatch.getTotalTimeMillis(),
                Instant.now().atZone(ZoneId.systemDefault()), ZoneId.systemDefault(),
                this.ipHelper.getServerPort(), this.mafProjectProperties.getContextPath(),
                this.ipHelper.getPublicIp(), this.ipHelper.getServerPort(), this.mafProjectProperties.getContextPath()
        );
        this.applicationContext.getAutowireCapableBeanFactory().destroyBean(this);
    }

    @Override
    public void destroy() {
        log.warn("Destroyed bean: {}", this.getClass().getSimpleName());
    }
}
