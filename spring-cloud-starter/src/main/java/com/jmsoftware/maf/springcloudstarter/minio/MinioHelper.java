package com.jmsoftware.maf.springcloudstarter.minio;

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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    public boolean bucketExists(@NotBlank String bucket) {
        return this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    }

    @SneakyThrows
    @SuppressWarnings("UnusedReturnValue")
    public boolean makeBucket(@NotBlank String bucket) {
        if (this.bucketExists(bucket)) {
            log.warn("The bucket named '{}' exists", bucket);
            return false;
        }
        this.minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        return true;
    }

    @SneakyThrows
    public ObjectWriteResponse put(@NotBlank String bucket, @NotBlank String object,
                                   @NotNull MultipartFile multipartFile) {
        return this.minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(object)
                        .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                        .contentType(multipartFile.getContentType())
                        .build());
    }

    @SneakyThrows
    public ObjectWriteResponse putObject(@NotBlank String bucket, @NotBlank String object,
                                         @NotNull InputStream inputStream, @NotBlank String contentType) {
        return this.minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(bucket)
                        .object(object)
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
    public boolean removeBucket(@NotBlank String bucket) {
        if (!this.bucketExists(bucket)) {
            return false;
        }
        val myObjects = this.listObjects(bucket);
        for (val result : myObjects) {
            val item = result.get();
            // If item has files, fail to remove
            if (item.size() > 0) {
                return false;
            }
        }
        // If bucket is empty, then it can be removed
        this.minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
        return !this.bucketExists(bucket);
    }

    @SneakyThrows
    public List<String> listObjectNames(@NotBlank String bucket) {
        val listObjectNames = new LinkedList<String>();
        if (!this.bucketExists(bucket)) {
            return listObjectNames;
        }
        val myObjects = this.listObjects(bucket);
        for (val result : myObjects) {
            val item = result.get();
            listObjectNames.add(item.objectName());
        }
        return listObjectNames;
    }

    @SneakyThrows
    public Iterable<Result<Item>> listObjects(@NotBlank String bucket) {
        if (!this.bucketExists(bucket)) {
            return null;
        }
        return this.minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
    }

    @SneakyThrows
    public GetObjectResponse getObject(@NotBlank String bucket, @NotBlank String object) {
        if (!this.bucketExists(bucket)) {
            return null;
        }
        val statObjectResponse = this.statObject(bucket, object);
        if (ObjectUtil.isNull(statObjectResponse) || statObjectResponse.size() == 0) {
            return null;
        }
        return this.minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(object).build());
    }

    @SneakyThrows
    public GetObjectResponse getObject(@NotBlank String bucket, @NotBlank String object, @Min(0L) long offset,
                                       @NotNull @Min(0L) Long length) {
        if (!this.bucketExists(bucket)) {
            return null;
        }
        val statObjectResponse = this.statObject(bucket, object);
        if (ObjectUtil.isNull(statObjectResponse) || statObjectResponse.size() == 0) {
            return null;
        }
        return this.minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(object).offset(offset).length(length).build());
    }

    @SneakyThrows
    public boolean removeObjects(String bucket, String object) {
        if (!this.bucketExists(bucket)) {
            return false;
        }
        this.minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(object).build());
        return true;
    }

    @SneakyThrows
    public List<String> removeObjects(String bucket, List<String> objects) {
        val deleteErrorNameList = new LinkedList<String>();
        if (this.bucketExists(bucket)) {
            val deleteObjectList = objects.stream().map(DeleteObject::new).collect(Collectors.toList());
            val results = this.minioClient.removeObjects(
                    RemoveObjectsArgs.builder().bucket(bucket).objects(deleteObjectList).build());
            for (val result : results) {
                val error = result.get();
                deleteErrorNameList.add(error.objectName());
            }
        }
        return deleteErrorNameList;
    }

    @SneakyThrows
    public String getPresignedObjectUrl(@NotBlank String bucket, @NotBlank String object,
                                        @NotNull Method method,
                                        @NotNull @Min(1L) @Max(DEFAULT_EXPIRY_TIME) Integer expiration) {
        if (!this.bucketExists(bucket)) {
            return null;
        }
        return this.minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs
                        .builder()
                        .bucket(bucket)
                        .object(object)
                        .method(method)
                        .expiry(expiration)
                        .build());
    }

    @SneakyThrows
    public StatObjectResponse statObject(@NotBlank String bucket, @NotBlank String object) {
        if (!this.bucketExists(bucket)) {
            return null;
        }
        return this.minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(object).build());
    }

    @SneakyThrows
    public String getPresignedObjectUrl(@NotBlank String bucket, @NotBlank String object) {
        if (!this.bucketExists(bucket)) {
            return null;
        }
        return this.minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder().bucket(bucket).object(object).build());
    }

    @SneakyThrows
    public ObjectWriteResponse composeObject(@NotBlank String bucket, @NotBlank String object,
                                             @NotEmpty List<ComposeSource> sources,
                                             @Nullable Map<String, String> headers) {
        if (!this.bucketExists(bucket)) {
            return null;
        }
        return this.minioClient.composeObject(
                ComposeObjectArgs.builder().bucket(bucket).object(object).sources(sources).headers(headers).build());
    }
}
