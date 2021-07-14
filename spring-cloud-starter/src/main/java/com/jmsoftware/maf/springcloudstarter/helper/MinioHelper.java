package com.jmsoftware.maf.springcloudstarter.helper;

import cn.hutool.core.util.ObjectUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
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
        return this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    @SneakyThrows
    @SuppressWarnings("UnusedReturnValue")
    public boolean makeBucket(@NotBlank String bucketName) {
        if (this.bucketExists(bucketName)) {
            log.warn("The bucket named '{}' exists", bucketName);
            return false;
        }
        this.minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        return true;
    }

    @SneakyThrows
    public ObjectWriteResponse put(@NotBlank String bucketName, @NotBlank String objectName,
                                   @NotNull MultipartFile multipartFile) {
        return this.minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                        .contentType(multipartFile.getContentType())
                        .build());
    }

    @SneakyThrows
    public ObjectWriteResponse putObject(@NotBlank String bucketName, @NotBlank String objectName,
                                         @NotNull InputStream inputStream, @NotBlank String contentType) {
        return this.minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .contentType(contentType)
                        .build());
    }

    @SneakyThrows
    public List<String> listBucketNames() {
        return this.listBuckets().stream().map(Bucket::name).collect(Collectors.toList());
    }

    @SneakyThrows
    public List<Bucket> listBuckets() {
        return this.minioClient.listBuckets();
    }

    @SneakyThrows
    public boolean removeBucket(@NotBlank String bucketName) {
        if (!this.bucketExists(bucketName)) {
            return false;
        }
        val myObjects = this.listObjects(bucketName);
        for (val result : myObjects) {
            val item = result.get();
            // If item has files, fail to remove
            if (item.size() > 0) {
                return false;
            }
        }
        // If bucket is empty, then it can be removed
        this.minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        return !this.bucketExists(bucketName);
    }

    @SneakyThrows
    public List<String> listObjectNames(@NotBlank String bucketName) {
        val listObjectNames = new LinkedList<String>();
        if (!this.bucketExists(bucketName)) {
            return listObjectNames;
        }
        val myObjects = this.listObjects(bucketName);
        for (val result : myObjects) {
            val item = result.get();
            listObjectNames.add(item.objectName());
        }
        return listObjectNames;
    }

    @SneakyThrows
    public Iterable<Result<Item>> listObjects(@NotBlank String bucketName) {
        if (!this.bucketExists(bucketName)) {
            return null;
        }
        return this.minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
    }

    @SneakyThrows
    public GetObjectResponse getObject(@NotBlank String bucketName, @NotBlank String objectName) {
        if (!this.bucketExists(bucketName)) {
            return null;
        }
        val statObjectResponse = this.statObject(bucketName, objectName);
        if (ObjectUtil.isNull(statObjectResponse) || statObjectResponse.size() == 0) {
            return null;
        }
        return this.minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    @SneakyThrows
    public GetObjectResponse getObject(@NotBlank String bucketName, @NotBlank String objectName, @Min(0L) long offset,
                                       @NotNull @Min(0L) Long length) {
        if (!this.bucketExists(bucketName)) {
            return null;
        }
        val statObjectResponse = this.statObject(bucketName, objectName);
        if (ObjectUtil.isNull(statObjectResponse) || statObjectResponse.size() == 0) {
            return null;
        }
        return this.minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(objectName).offset(offset).length(length).build());
    }

    @SneakyThrows
    public boolean removeObject(String bucketName, String objectName) {
        if (!this.bucketExists(bucketName)) {
            return false;
        }
        this.minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        return true;
    }

    @SneakyThrows
    public List<String> removeObject(String bucketName, List<String> objectNames) {
        val deleteErrorNameList = new LinkedList<String>();
        if (this.bucketExists(bucketName)) {
            val deleteObjectList = objectNames.stream().map(DeleteObject::new).collect(Collectors.toList());
            val results = this.minioClient.removeObjects(
                    RemoveObjectsArgs.builder().bucket(bucketName).objects(deleteObjectList).build());
            for (val result : results) {
                val error = result.get();
                deleteErrorNameList.add(error.objectName());
            }
        }
        return deleteErrorNameList;
    }

    @SneakyThrows
    public String getPresignedObjectUrl(@NotBlank String bucketName, @NotBlank String objectName,
                                        @NotNull Method method,
                                        @NotNull @Min(1L) @Max(DEFAULT_EXPIRY_TIME) Integer expiration) {
        if (!this.bucketExists(bucketName)) {
            return null;
        }
        return this.minioClient.getPresignedObjectUrl(
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
        if (!this.bucketExists(bucketName)) {
            return null;
        }
        return this.minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    @SneakyThrows
    public String getPresignedObjectUrl(@NotBlank String bucketName, @NotBlank String objectName) {
        if (!this.bucketExists(bucketName)) {
            return null;
        }
        return this.minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).build());
    }
}
