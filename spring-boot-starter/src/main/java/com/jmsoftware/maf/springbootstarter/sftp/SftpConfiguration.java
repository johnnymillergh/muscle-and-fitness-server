package com.jmsoftware.maf.springbootstarter.sftp;

import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.messaging.MessageHandler;

import java.io.File;

/**
 * Description: SftpConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/29/2021 2:09 PM
 **/
@Slf4j
@Configuration
@Import({
        SftpClientConfiguration.class
})
public class SftpConfiguration {
    @Bean
    public SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory(SftpClientConfiguration sftpClientConfiguration) {
        val factory = new DefaultSftpSessionFactory(true);
        factory.setHost(sftpClientConfiguration.getHost());
        factory.setPort(sftpClientConfiguration.getPort());
        factory.setUser(sftpClientConfiguration.getUser());
        if (sftpClientConfiguration.getPrivateKey() != null) {
            factory.setPrivateKey(sftpClientConfiguration.getPrivateKey());
            factory.setPrivateKeyPassphrase(sftpClientConfiguration.getPrivateKeyPassPhrase());
        } else {
            factory.setPassword(sftpClientConfiguration.getPassword());
        }
        factory.setAllowUnknownKeys(true);
        // We return a caching session factory, so that we don't have to reconnect to SFTP server for each time
        val cachingSessionFactory = new CachingSessionFactory<>(factory, sftpClientConfiguration.getSessionCacheSize());
        cachingSessionFactory.setSessionWaitTimeout(sftpClientConfiguration.getSessionWaitTimeout());
        log.warn("Initial bean: '{}'", cachingSessionFactory.getClass().getSimpleName());
        return cachingSessionFactory;
    }

    @Bean
    @ServiceActivator(inputChannel = "toSftpChannel")
    @SuppressWarnings("UnresolvedMessageChannel")
    public MessageHandler messageHandler(SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory,
                                         SftpClientConfiguration sftpClientConfiguration) {
        val handler = new SftpMessageHandler(sftpSessionFactory);
        handler.setRemoteDirectoryExpression(new LiteralExpression(sftpClientConfiguration.getDirectory()));
        handler.setFileNameGenerator(message -> {
            if (message.getPayload() instanceof File) {
                return ((File) message.getPayload()).getName();
            } else {
                throw new IllegalArgumentException("File expected as payload.");
            }
        });
        log.warn("Initial bean: '{}'", handler.getClass().getSimpleName());
        return handler;
    }

    @Bean
    public SftpRemoteFileTemplate sftpRemoteFileTemplate(SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory,
                                                         SftpClientConfiguration sftpClientConfiguration) {
        val sftpRemoteFileTemplate = new SftpRemoteFileTemplate(sftpSessionFactory);
        sftpRemoteFileTemplate.setRemoteDirectoryExpression(
                new LiteralExpression(sftpClientConfiguration.getDirectory()));
        sftpRemoteFileTemplate.setAutoCreateDirectory(true);
        // sftpRemoteFileTemplate.setBeanFactory(beanFactory);
        sftpRemoteFileTemplate.afterPropertiesSet();
        log.warn("Initial bean: '{}'", sftpRemoteFileTemplate.getClass().getSimpleName());
        return sftpRemoteFileTemplate;
    }

    @Bean
    public SftpSubDirectoryRunner sftpSubDirectoryRunner(SftpRemoteFileTemplate sftpRemoteFileTemplate,
                                                         SftpClientConfiguration sftpClientConfiguration) {
        log.warn("Initial bean: '{}'", SftpSubDirectoryRunner.class.getSimpleName());
        return new SftpSubDirectoryRunner(sftpRemoteFileTemplate, sftpClientConfiguration);
    }

    @Bean
    public SftpHelper sftpHelper(SftpRemoteFileTemplate sftpRemoteFileTemplate) {
        log.warn("Initial bean: '{}'", SftpHelper.class.getSimpleName());
        return new SftpHelperImpl(sftpRemoteFileTemplate);
    }
}
