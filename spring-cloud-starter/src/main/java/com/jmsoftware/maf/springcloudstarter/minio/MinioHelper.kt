package com.jmsoftware.maf.springcloudstarter.minio

import cn.hutool.core.collection.CollUtil
import com.google.common.collect.Lists
import com.jmsoftware.maf.common.util.logger
import io.minio.*
import io.minio.http.Method
import io.minio.messages.Bucket
import io.minio.messages.DeleteObject
import io.minio.messages.Item
import org.springframework.validation.annotation.Validated
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import javax.validation.constraints.*

/**
 * # MinioHelper
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/18/22 9:20 PM
 */
@Suppress("unused")
@Validated
class MinioHelper(
    private val minioClient: MinioClient
) {
    companion object {
        /**
         * 7 days
         */
        private const val DEFAULT_EXPIRY_TIME = 7 * 24 * 3600
        private val log = logger()
    }

    fun bucketExists(bucket: @NotBlank String?): Boolean {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())
    }

    fun makeBucket(bucket: @NotBlank String?): Boolean {
        if (bucketExists(bucket)) {
            MinioHelper.log.warn("The bucket named `$bucket` exists")
            return false
        }
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
        return true
    }

    fun put(
        bucket: @NotBlank String,
        `object`: @NotBlank String,
        multipartFile: @NotNull MultipartFile
    ): ObjectWriteResponse {
        return minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(`object`)
                .stream(multipartFile.inputStream, multipartFile.size, -1)
                .contentType(multipartFile.contentType)
                .build()
        )
    }

    fun putObject(
        bucket: @NotBlank String,
        `object`: @NotBlank String,
        inputStream: @NotNull InputStream,
        contentType: @NotBlank String
    ): ObjectWriteResponse {
        return minioClient.putObject(
            PutObjectArgs
                .builder()
                .bucket(bucket)
                .`object`(`object`)
                .stream(inputStream, inputStream.available().toLong(), -1)
                .contentType(contentType)
                .build()
        )
    }

    fun listBucketNames(): List<String> {
        return listBuckets().stream().map { obj: Bucket -> obj.name() }.toList()
    }

    fun listBuckets(): List<Bucket> {
        return minioClient.listBuckets()
    }

    fun removeBucket(bucket: @NotBlank String?): Boolean {
        if (!bucketExists(bucket)) {
            return false
        }
        val myObjects = listObjects(bucket)
        if (CollUtil.isEmpty(myObjects)) {
            return false
        }
        for (result in myObjects!!) {
            val item = result.get()
            // If item has files, fail to remove
            if (item.size() > 0) {
                return false
            }
        }
        // If bucket is empty, then it can be removed
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build())
        return !bucketExists(bucket)
    }

    fun listObjectNames(bucket: @NotBlank String): List<String> {
        val objectNames = Lists.newArrayList<String>()
        if (!bucketExists(bucket)) {
            return objectNames
        }
        val objects = listObjects(bucket)
        if (CollUtil.isEmpty(objects)) {
            return objectNames
        }
        for (result in objects!!) {
            val item = result.get()
            objectNames.add(item.objectName())
        }
        return objectNames
    }

    fun listObjects(bucket: @NotBlank String?): Iterable<Result<Item>>? {
        return if (!bucketExists(bucket)) {
            null
        } else minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).build())
    }

    fun getObject(bucket: @NotBlank String, `object`: @NotBlank String): GetObjectResponse? {
        if (!bucketExists(bucket)) {
            return null
        }
        val statObjectResponse = statObject(bucket, `object`)
        return if (statObjectResponse == null || statObjectResponse.size() == 0L) {
            null
        } else minioClient.getObject(GetObjectArgs.builder().bucket(bucket).`object`(`object`).build())
    }

    fun getObject(
        bucket: @NotBlank String,
        `object`: @NotBlank String,
        offset: @Min(0L) Long,
        length: @NotNull @Min(0L) Long
    ): GetObjectResponse? {
        if (!bucketExists(bucket)) {
            return null
        }
        val statObjectResponse = statObject(bucket, `object`)
        return if (statObjectResponse == null || statObjectResponse.size() == 0L) {
            null
        } else minioClient.getObject(
            GetObjectArgs.builder().bucket(bucket).`object`(`object`).offset(offset).length(length).build()
        )
    }

    fun removeObjects(bucket: @NotBlank String?, `object`: @NotBlank String?): Boolean {
        if (!bucketExists(bucket)) {
            return false
        }
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).`object`(`object`).build())
        return true
    }

    fun removeObjects(bucket: @NotBlank String, objects: @NotEmpty List<String>): List<String> {
        val deleteErrorNameList = mutableListOf<String>()
        if (bucketExists(bucket)) {
            val deleteObjectList = objects.stream().map { name: String? -> DeleteObject(name) }
                .toList()
            val results = minioClient.removeObjects(
                RemoveObjectsArgs.builder().bucket(bucket).objects(deleteObjectList).build()
            )
            for (result in results) {
                val error = result.get()
                deleteErrorNameList.add(error.objectName())
            }
        }
        return deleteErrorNameList
    }

    fun getPresignedObjectUrl(
        bucket: @NotBlank String,
        `object`: @NotBlank String,
        method: @NotNull Method,
        expiration: @NotNull @Min(1L) @Max(DEFAULT_EXPIRY_TIME.toLong()) Int = DEFAULT_EXPIRY_TIME
    ): String? {
        return if (!bucketExists(bucket)) {
            null
        } else minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs
                .builder()
                .bucket(bucket)
                .`object`(`object`)
                .method(method)
                .expiry(expiration)
                .build()
        )
    }

    fun statObject(bucket: @NotBlank String, `object`: @NotBlank String): StatObjectResponse? {
        return if (!bucketExists(bucket)) {
            null
        } else minioClient.statObject(StatObjectArgs.builder().bucket(bucket).`object`(`object`).build())
    }

    fun getPresignedObjectUrl(bucket: @NotBlank String, `object`: @NotBlank String): String? {
        return if (!bucketExists(bucket)) {
            null
        } else minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder().bucket(bucket).`object`(`object`).build()
        )
    }

    fun composeObject(
        bucket: @NotBlank String,
        `object`: @NotBlank String,
        sources: @NotEmpty MutableList<ComposeSource>,
        headers: Map<String, String>?
    ): ObjectWriteResponse? {
        return if (!bucketExists(bucket)) {
            null
        } else minioClient.composeObject(
            ComposeObjectArgs.builder().bucket(bucket).`object`(`object`).sources(sources).headers(headers).build()
        )
    }
}
