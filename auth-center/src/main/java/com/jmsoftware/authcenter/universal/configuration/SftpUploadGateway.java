package com.jmsoftware.authcenter.universal.configuration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * <h1>SftpUploadGateway</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-07-04 21:03
 **/
@Component
@MessagingGateway
public interface SftpUploadGateway {
    /**
     * Upload file
     *
     * @param file file
     */
    @Gateway(requestChannel = "toSftpChannel")
    @SuppressWarnings("UnresolvedMessageChannel")
    void upload(File file);
}
