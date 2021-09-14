package com.jmsoftware.maf.springcloudstarter.sftp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

/**
 * <h1>SftpSubDirectoryRunner</h1>
 * <p>After dependency injection finished, we must inti the SFTP server's sub-directory for out business. If you want
 * to customize initialization configuration, config SftpSubDirectory.</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-07-05 08:51
 * @see SftpSubDirectory
 **/
@Slf4j
@RequiredArgsConstructor
public class SftpSubDirectoryRunner implements ApplicationRunner {
    private final SftpRemoteFileTemplate sftpRemoteFileTemplate;
    private final SftpClientConfiguration sftpClientConfiguration;

    @Override
    public void run(ApplicationArguments args) {
        this.sftpRemoteFileTemplate.setAutoCreateDirectory(true);
        this.sftpRemoteFileTemplate.execute(session -> {
            if (!session.exists(this.sftpClientConfiguration.getDirectory())) {
                log.info("Make directories for SFTP server. Directory: {}", this.sftpClientConfiguration.getDirectory());
                session.mkdir(this.sftpClientConfiguration.getDirectory());
            } else {
                log.info("SFTP server remote directory exists: {}", this.sftpClientConfiguration.getDirectory());
            }
            return null;
        });

        log.info("Staring to initial SFTP server sub-directory.");
        this.sftpRemoteFileTemplate.execute(session -> {
            for (val sftpSubDirectory : SftpSubDirectory.values()) {
                val fullPath = this.sftpClientConfiguration.getDirectory() + sftpSubDirectory.getSubDirectory();
                if (!session.exists(fullPath)) {
                    log.info("SFTP server sub-directory does not exist. Creating sub-directory: {}", fullPath);
                    session.mkdir(fullPath);
                } else {
                    log.info("SFTP server sub-directory exists. Path: {}", fullPath);
                }
            }
            return null;
        });
        log.warn("Initialing SFTP server sub-directory is done.");
    }
}
