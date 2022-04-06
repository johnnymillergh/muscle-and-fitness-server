package com.jmsoftware.maf.springcloudstarter.helper;

import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;

import java.time.Instant;
import java.time.ZoneId;

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
    private static final String TEMPLATE ="""

            🥳 Congratulations! 🎉
            🖥 {}@{} started!
            ⚙️ Environment: {}
            ⏳ Deployment duration: {} seconds ({} ms)
            ⏰ App started at {} (timezone - {})
              App running at
              - Local:   http://localhost:{}{}/
              - Network: http://{}:{}{}/
            """;
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
