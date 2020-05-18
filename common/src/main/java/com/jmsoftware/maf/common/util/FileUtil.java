package com.jmsoftware.maf.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * <h1>FileUtil</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 2/27/20 9:45 AM
 **/
@Slf4j
@SuppressWarnings("unused")
public class FileUtil {
    /**
     * Convert multipart file to file
     *
     * @param multipartFile multipart file
     * @return file
     * @throws IOException IO exception
     */
    public static File convertFrom(final MultipartFile multipartFile) throws IOException {
        log.info("Converting multipart file, original multipart file name: {}", multipartFile.getOriginalFilename());
        var convertFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        var fos = new FileOutputStream(convertFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convertFile;
    }

    /**
     * Convert input stream to file
     *
     * @param inputStream input stream
     * @param savePath    path for saving file
     * @return file
     */
    public static File convertFrom(final InputStream inputStream, final String savePath) {
        log.info("Converting InputStream to file, save path: {}", savePath);
        var file = new File(savePath);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            log.error("Exception occurred when initializing FileOutputStream. Exception message: {}",
                      e.getMessage(),
                      e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("Exception occurred when closing FileOutputStream. Exception message: {}", e.getMessage(), e);
            }
        }
        return file;
    }

    /**
     * Generate date-format storage path (relative path, w/o SFTP's server directory)
     *
     * @param sftpSubDirectory SFTP server's sub directory
     * @return full storage path (absolute path). Null if file name is empty.
     */
    public static String generateDateFormatStoragePath(final String sftpSubDirectory) {
        var today = new Date();
        var year = DateUtil.format(today, "yyyy");
        var month = DateUtil.format(today, "MM");
        var day = DateUtil.format(today, "dd");
        return sftpSubDirectory
                + year
                + "/" + month
                + "/" + day
                + "/";
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
