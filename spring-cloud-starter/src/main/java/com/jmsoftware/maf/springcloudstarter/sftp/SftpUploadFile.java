package com.jmsoftware.maf.springcloudstarter.sftp;

import lombok.Builder;
import lombok.Data;
import org.springframework.integration.file.support.FileExistsMode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * <h1>SftpUploadFile</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-07-06 11:22
 **/
@Data
@Builder
public class SftpUploadFile {
    /**
     * File to be uploaded to SFTP server
     */
    @NotNull
    private File fileToBeUploaded;
    /**
     * SFTP server's sub directory (if sub directory does'nt exist, will be auto created). Not empty and it looks
     * like this: "/some/sub/directory/"
     */
    @NotEmpty
    private String subDirectory;
    /**
     * This enumeration indicates what action shall be taken in case the destination file already exists. In default,
     * it should be set as: FileExistsMode.REPLACE
     */
    @NotNull
    private FileExistsMode fileExistsMode;
}
