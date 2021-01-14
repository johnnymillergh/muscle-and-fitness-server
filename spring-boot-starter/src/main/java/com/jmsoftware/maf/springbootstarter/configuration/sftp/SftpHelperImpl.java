package com.jmsoftware.maf.springbootstarter.configuration.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jmsoftware.maf.springbootstarter.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Description: SftpHelperImpl, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/14/2021 2:50 PM
 **/
@Slf4j
@RequiredArgsConstructor
public class SftpHelperImpl implements SftpHelper {
    private final SftpRemoteFileTemplate sftpRemoteFileTemplate;

    @Override
    public List<String> listFiles(@NotBlank String fullPath) {
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
    public boolean exist(@NotBlank String fileFullPath) {
        log.info("Checking whether file exists in SFTP server, file full path: {}", fileFullPath);
        return sftpRemoteFileTemplate.execute(session -> session.exists(fileFullPath));
    }

    @Override
    public Long getFileSize(@NotBlank String fileFullPath) throws IllegalArgumentException {
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
        return sftpRemoteFileTemplate.send(message, sftpUploadFile.getSubDirectory(),
                                           sftpUploadFile.getFileExistsMode());
    }

    @Override
    public String upload(@NotNull MultipartFile multipartFile, @NotBlank String subDirectory,
                         @NotNull FileExistsMode fileExistsMode, boolean deleteSource) throws IOException {
        log.info("Uploading single multipart file to SFTP server. File name: {}", multipartFile.getOriginalFilename());
        File file = FileUtil.convertFrom(multipartFile);
        SftpUploadFile sftpUploadFile = SftpUploadFile.builder()
                .fileToBeUploaded(file)
                .subDirectory(subDirectory)
                .fileExistsMode(fileExistsMode)
                .build();
        String fileFullPath = this.upload(sftpUploadFile);
        if (deleteSource) {
            val deleted = file.delete();
            log.debug("File deleted: {} {}", deleted, file);
        }
        return fileFullPath;
    }

    @Override
    public InputStream read(@NotBlank String fileFullPath) throws IllegalArgumentException {
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
    public boolean delete(@NotBlank String fileFullPath) {
        log.warn("Deleting SFTP server's file by file full path: {}", fileFullPath);
        return sftpRemoteFileTemplate.execute(session -> {
            boolean existFile = session.exists(fileFullPath);
            if (existFile) {
                return session.remove(fileFullPath);
            } else {
                log.error("Cannot delete SFTP server's file. Caused by: file does not exist, full path: {} ",
                          fileFullPath);
                return false;
            }
        });
    }
}
