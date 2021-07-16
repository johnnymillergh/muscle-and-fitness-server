package com.jmsoftware.maf.springcloudstarter.helper;

import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
@Component
@RequiredArgsConstructor
public class SpringBootStartupHelper {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private final MafProjectProperty mafProjectProperty;
    private final IpHelper ipHelper;

    public void stop(@NonNull StopWatch stopWatch) {
        stopWatch.stop();
        log.info("🥳 Congratulations! 🎉");
        log.info("🖥 {}@{} started!", this.mafProjectProperty.getProjectArtifactId(), this.mafProjectProperty.getVersion());
        log.info("⚙️ Environment: {}", this.mafProjectProperty.getEnvironment());
        log.info("⏳ Deployment duration: {} seconds ({} ms)", stopWatch.getTotalTimeSeconds(),
                 stopWatch.getTotalTimeMillis());
        log.info("⏰ App started at {} (timezone - {})", Instant.now().atZone(ZoneId.of("UTC+8")), TimeZone.getDefault().getDisplayName());
        log.info("{}  App running at{}  - Local:   http://localhost:{}{}/{}  - Network: http://{}:{}{}/",
                 LINE_SEPARATOR, LINE_SEPARATOR, this.ipHelper.getServerPort(), this.mafProjectProperty.getContextPath(),
                 LINE_SEPARATOR, this.ipHelper.getPublicIp(), this.ipHelper.getServerPort(), this.mafProjectProperty.getContextPath());
    }
}
