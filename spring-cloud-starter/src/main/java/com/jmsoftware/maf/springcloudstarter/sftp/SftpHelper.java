package com.jmsoftware.maf.springcloudstarter.sftp;

import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Description: SftpHelper, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/14/2021 2:49 PM
 **/
@Validated
public interface SftpHelper {
    /**
     * List all files under the full path
     *
     * @param fullPath directory full path
     * @return file names
     */
    List<String> listFiles(@NotBlank String fullPath);

    /**
     * Check whether file exists according to file path
     *
     * @param fileFullPath file's full path
     * @return true - file exists; false - file not exists
     */
    boolean exist(@NotBlank String fileFullPath);

    /**
     * Get file size
     *
     * @param fileFullPath file's full path
     * @return file size (size unit: byte). Null if the file does not exist or path refers to a directory
     * @throws IllegalArgumentException when file does not exist
     */
    Long getFileSize(@NotBlank String fileFullPath) throws IllegalArgumentException;

    /**
     * Upload single file
     *
     * @param sftpUploadFile encapsulated object
     * @return file's full path if successful, else null
     */
    String upload(@Valid SftpUploadFile sftpUploadFile);

    /**
     * Upload file
     *
     * @param multipartFile  multipart file
     * @param subDirectory   SFTP server's sub directory (if sub directory doesn't exist, will be auto created). Not
     *                       empty and it looks like this: "/some/sub/directory/"
     * @param fileExistsMode This enumeration indicates what action shall be taken in case the destination file
     *                       already exists. In default, it should be set as: FileExistsMode.REPLACE
     * @param deleteSource   true - delete source file; false - not delete source file
     * @return file's full path if successful, else null
     * @throws IOException IO exception
     */
    String upload(@NotNull MultipartFile multipartFile, @NotBlank String subDirectory,
                  @NotNull FileExistsMode fileExistsMode, boolean deleteSource) throws IOException;

    /**
     * Read file from SFTP server
     *
     * @param fileFullPath file's full path
     * @return file's stream
     * @throws IllegalArgumentException when file does not exist
     */
    InputStream read(@NotBlank String fileFullPath) throws IllegalArgumentException;

    /**
     * Delete file according to file path
     *
     * @param fileFullPath file's full path
     * @return true - file deleted; false - file not deleted
     */
    boolean delete(@NotBlank String fileFullPath);
}
