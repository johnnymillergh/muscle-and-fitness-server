package com.jmsoftware.maf.apiportal.universal.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jmsoftware.maf.apiportal.universal.configuration.SftpClientConfiguration;
import com.jmsoftware.maf.apiportal.universal.configuration.SftpUploadGateway;
import com.jmsoftware.maf.apiportal.universal.domain.SftpUploadFile;
import com.jmsoftware.maf.apiportal.universal.service.SftpService;
import com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * <h1>SftpServiceImpl</h1>
 * <p>SFTP Service implementation</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-07-04 20:47
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class SftpServiceImpl implements SftpService {
    private final SftpRemoteFileTemplate sftpRemoteFileTemplate;
    private final SftpUploadGateway sftpUploadGateway;
    private final SftpClientConfiguration sftpClientConfiguration;
    private final BeanFactory beanFactory;

    @Override
    public List<String> listFiles(String fullPath) {
        log.info("Listing files, full path: {}", fullPath);
        return sftpRemoteFileTemplate.execute(session -> {
            String[] strings = new String[0];
            try {
                strings = session.listNames(fullPath);
            } catch (IOException e) {
                log.error("Exception occurred when listing files. Exception message: {}", e.getMessage(), e);
            }
            return Arrays.asList(strings);
        });
    }

    @Override
    public boolean exist(String fileFullPath) {
        log.info("Checking whether file exists in SFTP server, file full path: {}", fileFullPath);
        return sftpRemoteFileTemplate.execute(session -> session.exists(fileFullPath));
    }

    @Override
    public Long getFileSize(String fileFullPath) throws IllegalArgumentException {
        if (!exist(fileFullPath)) {
            throw new IllegalArgumentException(
                    "Cannot get file size from SFTP server. Caused by: file does not exist, full path: " + fileFullPath);
        }
        String[] splits = fileFullPath.split("/");
        String fileName = splits[splits.length - 1];
        String listPath = fileFullPath.substring(0, fileFullPath.lastIndexOf(fileName) - 1);
        log.info("Retrieve file size from SFTP server, full path: {}", fileFullPath);
        final Long[] fileSize = new Long[1];
        sftpRemoteFileTemplate.execute(session -> {
            ChannelSftp.LsEntry[] lsEntries = session.list(listPath);
            for (ChannelSftp.LsEntry lsEntry : lsEntries) {
                if (lsEntry.getFilename().equals(fileName)) {
                    fileSize[0] = lsEntry.getAttrs().getSize();
                }
            }
            return null;
        });
        return fileSize[0];
    }

    @Override
    public String upload(@Valid SftpUploadFile sftpUploadFile) {
        log.info("Uploading single file to SFTP server. SftpUploadFile: {}", sftpUploadFile);
        Message<File> message = MessageBuilder.withPayload(sftpUploadFile.getFileToBeUploaded()).build();
        sftpRemoteFileTemplate.setRemoteDirectoryExpression(
                new LiteralExpression(sftpClientConfiguration.getDirectory()));
        sftpRemoteFileTemplate.setAutoCreateDirectory(true);
        sftpRemoteFileTemplate.setBeanFactory(beanFactory);
        // sftpRemoteFileTemplate.setCharset("UTF-8");
        sftpRemoteFileTemplate.afterPropertiesSet();
        return sftpRemoteFileTemplate.send(message,
                                           sftpUploadFile.getSubDirectory(),
                                           sftpUploadFile.getFileExistsMode());
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String upload(MultipartFile multipartFile,
                         String subDirectory,
                         FileExistsMode fileExistsMode,
                         boolean deleteSource) throws IOException {
        log.info("Uploading single multipart file to SFTP server. File name: {}", multipartFile.getOriginalFilename());
        File file = FileUtil.convertFrom(multipartFile);
        SftpUploadFile sftpUploadFile = SftpUploadFile
                .builder()
                .fileToBeUploaded(file)
                .subDirectory(subDirectory)
                .fileExistsMode(fileExistsMode)
                .build();
        String fileFullPath = this.upload(sftpUploadFile);
        if (deleteSource) {
            file.delete();
        }
        return fileFullPath;
    }

    @Override
    public void upload(List<MultipartFile> files) throws IOException {
        upload(files, true);
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void upload(List<MultipartFile> files, boolean deleteSource) throws IOException {
        log.info("Uploading multipart files to SFTP server (delete source file: {}). Files: {}", deleteSource, files);
        for (MultipartFile multipartFile : files) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            File file = FileUtil.convertFrom(multipartFile);
            sftpUploadGateway.upload(file);
            if (deleteSource) {
                file.delete();
            }
        }
    }

    @Override
    public InputStream read(String fileFullPath) throws IllegalArgumentException {
        log.info("Read file from SFTP server, file full path: {}", fileFullPath);
        return sftpRemoteFileTemplate.execute(session -> {
            boolean existFile = session.exists(fileFullPath);
            if (existFile) {
                return session.readRaw(fileFullPath);
            }
            log.error("Cannot read file from SFTP 'server. Caused by : file does not exist, full path: {}",
                      fileFullPath);
            throw new IllegalArgumentException(
                    "Cannot read file from SFTP 'server. Caused by : file does not exist, full path: " + fileFullPath);
        });
    }

    @Override
    public boolean delete(String fileFullPath) {
        log.info("Deleting SFTP server's file by file full path: {}", fileFullPath);
        return sftpRemoteFileTemplate.execute(session -> {
            boolean existFile = session.exists(fileFullPath);
            if (existFile) {
                return session.remove(fileFullPath);
            } else {
                log.info("Cannot delete SFTP server's file. Caused by: file does not exist, full path: {} ",
                         fileFullPath);
                return false;
            }
        });
    }
}
