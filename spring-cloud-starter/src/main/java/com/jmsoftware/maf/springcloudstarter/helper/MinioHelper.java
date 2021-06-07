package com.jmsoftware.maf.springcloudstarter.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>MinioHelper</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/7/21 9:52 PM
 **/
@Slf4j
@Validated
@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class MinioHelper {
    /**
     * 7 days
     */
    private static final int DEFAULT_EXPIRY_TIME = 7 * 24 * 3600;
    private final MinioClient minioClient;

    @SneakyThrows
    public boolean bucketExists(@NotBlank String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    @SneakyThrows
    public boolean makeBucket(@NotBlank String bucketName) {
        val exists = bucketExists(bucketName);
        if (exists) {
            log.warn("The bucket named '{}' exists", bucketName);
            return false;
        }
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        return true;
    }

    @SneakyThrows
    public boolean put(@NotBlank String bucketName, @NotBlank String objectName, @NotNull MultipartFile multipartFile) {
        val objectWriteResponse = minioClient.putObject(
                PutObjectArgs
                        .builder().
                        bucket(bucketName)
                        .object(objectName)
                        .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                        .contentType(multipartFile.getContentType())
                        .build());
        log.info("Put multipart file: {}", JSONUtil.toJsonStr(objectWriteResponse));
        return true;
    }

    @SneakyThrows
    public boolean putObject(@NotBlank String bucketName, @NotBlank String objectName,
                             @NotNull InputStream inputStream, @NotBlank String contentType) {
        val objectWriteResponse = minioClient.putObject(
                PutObjectArgs
                        .builder().
                        bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .contentType(contentType)
                        .build());
        log.info("Put object: {}", JSONUtil.toJsonStr(objectWriteResponse));
        return true;
    }

    @SneakyThrows
    public List<String> listBucketNames() {
        return listBuckets().stream().map(Bucket::name).collect(Collectors.toList());
    }

    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }

    @SneakyThrows
    public boolean removeBucket(@NotBlank String bucketName) {
        val exists = bucketExists(bucketName);
        if (!exists) {
            return false;
        }
        Iterable<Result<Item>> myObjects = listObjects(bucketName);
        for (Result<Item> result : myObjects) {
            Item item = result.get();
            // If item has files, fail to remove
            if (item.size() > 0) {
                return false;
            }
        }
        // If bucket is empty, then it can be removed
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        return !bucketExists(bucketName);
    }

    @SneakyThrows
    public List<String> listObjectNames(@NotBlank String bucketName) {
        List<String> listObjectNames = new LinkedList<>();
        boolean exists = bucketExists(bucketName);
        if (!exists) {
            return listObjectNames;
        }
        Iterable<Result<Item>> myObjects = listObjects(bucketName);
        for (Result<Item> result : myObjects) {
            Item item = result.get();
            listObjectNames.add(item.objectName());
        }
        return listObjectNames;
    }

    @SneakyThrows
    public Iterable<Result<Item>> listObjects(@NotBlank String bucketName) {
        boolean exists = bucketExists(bucketName);
        if (exists) {
            return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        }
        return null;
    }

    @SneakyThrows
    public InputStream getObject(@NotBlank String bucketName, @NotBlank String objectName) {
        if (!bucketExists(bucketName)) {
            return null;
        }
        val statObjectResponse = statObject(bucketName, objectName);
        if (ObjectUtil.isNull(statObjectResponse) || statObjectResponse.size() == 0) {
            return null;
        }
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    @SneakyThrows
    public InputStream getObject(@NotBlank String bucketName, @NotBlank String objectName, @Min(0L) long offset,
                                 @NotNull @Min(0L) Long length) {
        if (!bucketExists(bucketName)) {
            return null;
        }
        val statObjectResponse = statObject(bucketName, objectName);
        if (ObjectUtil.isNull(statObjectResponse) || statObjectResponse.size() == 0) {
            return null;
        }
        return minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(objectName).offset(offset).length(length).build());
    }

    @SneakyThrows
    public boolean removeObject(String bucketName, String objectName) {
        if (!bucketExists(bucketName)) {
            return false;
        }
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        return true;
    }

    @SneakyThrows
    public List<String> removeObject(String bucketName, List<String> objectNames) {
        final List<String> deleteErrorNameList = new LinkedList<>();
        if (bucketExists(bucketName)) {
            val deleteObjectList = objectNames.stream().map(DeleteObject::new).collect(Collectors.toList());
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                    RemoveObjectsArgs.builder().bucket(bucketName).objects(deleteObjectList).build());
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                deleteErrorNameList.add(error.objectName());
            }
        }
        return deleteErrorNameList;
    }

    @SneakyThrows
    public String getPresignedObjectUrl(@NotBlank String bucketName, @NotBlank String objectName,
                                        @NotNull Method method,
                                        @NotNull @Min(1L) @Max(DEFAULT_EXPIRY_TIME) Integer expiration) {
        if (!bucketExists(bucketName)) {
            return null;
        }
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs
                        .builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .method(method)
                        .expiry(expiration)
                        .build());
    }

    @SneakyThrows
    public StatObjectResponse statObject(@NotBlank String bucketName, @NotBlank String objectName) {
        boolean exists = bucketExists(bucketName);
        if (!exists) {
            return null;
        }
        return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    @SneakyThrows
    public String getPresignedObjectUrl(@NotBlank String bucketName, @NotBlank String objectName) {
        if (!bucketExists(bucketName)) {
            return null;
        }
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).build());
    }

//    public void downloadFile(@NotBlank String bucketName, @NotBlank String fileName, String originalName,
//                             @NotNull HttpServletResponse response) {
//            InputStream file = minioClient.getObject(bucketName, fileName);
//            String filename = new String(fileName.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
//            if (StringUtils.isNotEmpty(originalName)) {
//                fileName = originalName;
//            }
//            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
//            ServletOutputStream servletOutputStream = response.getOutputStream();
//            int len;
//            byte[] buffer = new byte[1024];
//            while ((len = file.read(buffer)) > 0) {
//                servletOutputStream.write(buffer, 0, len);
//            }
//            servletOutputStream.flush();
//            file.close();
//            servletOutputStream.close();
//    }
}
