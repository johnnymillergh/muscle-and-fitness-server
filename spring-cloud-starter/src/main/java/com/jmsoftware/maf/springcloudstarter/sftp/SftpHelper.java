package com.jmsoftware.maf.springcloudstarter.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jmsoftware.maf.springcloudstarter.util.FileUtil;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Description: Sftp Helper
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 2/26/2021 3:47 PM
 */
@Slf4j
@Validated
@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SftpHelper {
    /**
     * The Sftp remote file template.
     */
    private final SftpRemoteFileTemplate sftpRemoteFileTemplate;

    /**
     * List all files under the full path
     *
     * @param fullPath directory full path
     * @return file names
     */
    public List<String> listFiles(@NotBlank String fullPath) {
        log.info("Listing files, full path: {}", fullPath);
        return sftpRemoteFileTemplate.execute(session -> {
            var strings = new String[0];
            try {
                strings = session.listNames(fullPath);
            } catch (IOException e) {
                log.error("Exception occurred when listing files. Exception message: {}", e.getMessage(), e);
            }
            return Arrays.asList(strings);
        });
    }

    /**
     * Check whether file exists according to file path
     *
     * @param fileFullPath file's full path
     * @return true - file exists; false - file not exists
     */
    public boolean exist(@NotBlank String fileFullPath) {
        log.info("Checking whether file exists in SFTP server, file full path: {}", fileFullPath);
        return sftpRemoteFileTemplate.execute(session -> session.exists(fileFullPath));
    }

    /**
     * Get file size
     *
     * @param fileFullPath file's full path
     * @return file size (size unit: byte). Null if the file does not exist or path refers to a directory
     * @throws IllegalArgumentException when file does not exist
     */
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

    /**
     * Upload single file
     *
     * @param sftpUploadFile encapsulated object
     * @return file 's full path if successful, else null
     */
    public String upload(@Valid SftpUploadFile sftpUploadFile) {
        log.info("Uploading single file to SFTP server. SftpUploadFile: {}", sftpUploadFile);
        Message<File> message = MessageBuilder.withPayload(sftpUploadFile.getFileToBeUploaded()).build();
        return sftpRemoteFileTemplate.send(message, sftpUploadFile.getSubDirectory(),
                                           sftpUploadFile.getFileExistsMode());
    }

    /**
     * Upload file
     *
     * @param multipartFile  multipart file
     * @param subDirectory   SFTP server's sub directory (if sub directory doesn't exist, will be auto created). Not
     *                       empty and it looks like this: "/some/sub/directory/"
     * @param fileExistsMode This enumeration indicates what action shall be taken in case the destination file
     *                       already exists. In default, it should be set as: FileExistsMode.REPLACE
     * @param deleteSource   true - delete source file; false - not delete source file
     * @return file full path if successful, else null
     * @throws IOException IO exception
     */
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
            log.debug("File deleted: {}, {}", deleted, file);
        }
        return fileFullPath;
    }

    /**
     * Read file from SFTP server
     *
     * @param fileFullPath file's full path
     * @return buffered file stream
     * @throws NotFoundException when file does not exist
     */
    public BufferedInputStream read(@NotBlank String fileFullPath) throws NotFoundException {
        log.info("Read file from SFTP server, file full path: {}", fileFullPath);
        val inputStream = new AtomicReference<InputStream>();
        val got = sftpRemoteFileTemplate.get(fileFullPath, inputStream::set);
        if (!got) {
            val errorMessage = String.format("Cannot find the file! fileFullPath: %s", fileFullPath);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return new BufferedInputStream(inputStream.get());
    }

    /**
     * Delete file according to file path
     *
     * @param fileFullPath file's full path
     * @return true - file deleted; false - file not deleted
     */
    public boolean delete(@NotBlank String fileFullPath) {
        log.warn("Deleting SFTP server's file by file full path: {}", fileFullPath);
        return sftpRemoteFileTemplate.remove(fileFullPath);
    }
}
