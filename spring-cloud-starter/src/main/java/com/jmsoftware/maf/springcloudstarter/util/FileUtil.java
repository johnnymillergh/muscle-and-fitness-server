package com.jmsoftware.maf.springcloudstarter.util;

import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * <h1>FileUtil</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2 /27/20 9:45 AM
 */
@Slf4j
@SuppressWarnings("unused")
public class FileUtil {
    private FileUtil() {
    }

    /**
     * Convert multipart file to file
     *
     * @param multipartFile multipart file
     * @return file file
     * @throws IOException IO exception
     */
    public static File convertFrom(@NonNull final MultipartFile multipartFile) throws IOException {
        log.info("Converting MultipartFile, original multipart file name: {}", multipartFile.getOriginalFilename());
        val newFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        multipartFile.transferTo(newFile);
        log.info("Converted MultipartFile to newFile: {}", newFile);
        return newFile;
    }

    /**
     * Convert input stream to file
     *
     * @param inputStream input stream
     * @param savePath    path for saving file
     * @return file file
     * @throws IOException the io exception
     */
    public static File convertFrom(final InputStream inputStream, final String savePath) throws IOException {
        log.info("Converting InputStream to file, save path: {}", savePath);
        val newFile = new File(savePath);
        FileUtils.copyInputStreamToFile(inputStream, newFile);
        log.info("Converted MultipartFile to newFile: {}", newFile);
        return newFile;
    }

    /**
     * Generate date-format storage path (relative path, w/o SFTP server directory)
     *
     * @param sftpSubDirectory SFTP server's sub directory
     * @return full storage path (absolute path). Null if file name is empty.
     */
    public static String generateDateFormatStoragePath(@NonNull final String sftpSubDirectory) {
        val localDate = LocalDate.now();
        return String.format("%s%d/%d/%d/", sftpSubDirectory, localDate.getYear(), localDate.getMonth().getValue(),
                             localDate.getDayOfMonth());
    }

    /**
     * Generate unique file name for file
     *
     * @param file file
     * @return unique file name (UUID.fileExtension)
     */
    public static String generateUniqueFileName(final File file) {
        var fileName = file.getName();
        if (StrUtil.isBlank(fileName)) {
            return null;
        }
        var fileExtension = fileName.substring(fileName.lastIndexOf("."));
        return UUID.randomUUID() + "." + fileExtension;
    }
}
