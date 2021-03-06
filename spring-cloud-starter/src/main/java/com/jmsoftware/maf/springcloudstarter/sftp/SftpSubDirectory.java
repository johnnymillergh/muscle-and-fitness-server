package com.jmsoftware.maf.springcloudstarter.sftp;

import lombok.Getter;

/**
 * <h1>SftpSubDirectory</h1>
 * <p>Reminder: if you want to add more custom sub directories in the future, please add en enum item in this class</p>
 *
 * TODO: think of another better way to configure the directory
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-07-04 23:10
 **/
@Getter
public enum SftpSubDirectory {
    /**
     * Sub directory for video
     */
    VIDEO("video", "/video/"),
    /**
     * Sub directory for avatar
     */
    AVATAR("avatar", "/avatar/");

    private final String directoryName;
    private final String subDirectory;

    SftpSubDirectory(String directoryName, String subDirectory) {
        this.directoryName = directoryName;
        this.subDirectory = subDirectory;
    }
}
