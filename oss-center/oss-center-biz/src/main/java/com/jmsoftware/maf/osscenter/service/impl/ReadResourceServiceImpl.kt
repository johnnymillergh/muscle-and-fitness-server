package com.jmsoftware.maf.osscenter.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.io.IoUtil
import cn.hutool.core.io.NioUtil
import com.jmsoftware.maf.common.exception.ResourceNotFoundException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.osscenter.constant.Chunk
import com.jmsoftware.maf.osscenter.constant.Chunk.LARGE_CHUNK_SIZE
import com.jmsoftware.maf.osscenter.response.SerializableStatObjectResponse
import com.jmsoftware.maf.osscenter.service.ReadResourceService
import com.jmsoftware.maf.springcloudstarter.minio.MinioHelper
import io.minio.StatObjectResponse
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.OutputStream

/**
 * # ReadResourceServiceImpl
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:48 PM
 */
@Service
class ReadResourceServiceImpl(
    private val minioHelper: MinioHelper
) : ReadResourceService {
    companion object {
        private const val ACCEPT_RANGES_VALUE = "bytes"
        private val log = logger()
    }

    override fun asyncGetSingleResource(
        bucket: String,
        `object`: String
    ): ResponseEntity<StreamingResponseBody> {
        val statObjectResponse: StatObjectResponse? = try {
            minioHelper.statObject(bucket, `object`)
        } catch (e: Exception) {
            log.error("Exception occurred when looking for object!", e)
            return ResponseEntity.notFound().build()
        }
        val objectResponse = minioHelper.getObject(bucket, `object`)
        if (statObjectResponse == null || objectResponse == null) {
            log.warn("Object not found! Bucket: {}, Object: {}", bucket, `object`)
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.ACCEPT_RANGES, ACCEPT_RANGES_VALUE)
            .contentLength(statObjectResponse.size())
            .contentType(MediaType.parseMediaType(statObjectResponse.contentType()))
            .body(StreamingResponseBody { outputStream: OutputStream? ->
                NioUtil.copyByNIO(objectResponse, outputStream, NioUtil.DEFAULT_BUFFER_SIZE, null)
                IoUtil.close(objectResponse)
            })
    }

    override fun asyncStreamSingleResource(
        bucket: String,
        `object`: String,
        range: String?
    ): ResponseEntity<StreamingResponseBody> {
        val statObjectResponse: StatObjectResponse? = try {
            minioHelper.statObject(bucket, `object`)
        } catch (e: Exception) {
            log.error(
                "Exception occurred when looking for object. Exception message: {}",
                e.message
            )
            return ResponseEntity.notFound().build()
        }
        if (statObjectResponse == null) {
            log.warn("StatObjectResponse not found! Bucket: {}, Object: {}", bucket, `object`)
            return ResponseEntity.notFound().build()
        }
        val httpRanges = HttpRange.parseRanges(range)
        if (CollUtil.isEmpty(httpRanges)) {
            val objectResponse = minioHelper.getObject(bucket, `object`, 0, Chunk.TINY_CHUNK_SIZE.toBytes())
            if (objectResponse == null) {
                log.warn("Object not found! Bucket: {}, Object: {}", bucket, `object`)
                return ResponseEntity.notFound().build()
            }
            return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT_RANGES, ACCEPT_RANGES_VALUE)
                .contentLength(statObjectResponse.size())
                .contentType(MediaType.parseMediaType(statObjectResponse.contentType()))
                .body(StreamingResponseBody { outputStream: OutputStream? ->
                    NioUtil.copyByNIO(objectResponse, outputStream, NioUtil.DEFAULT_BUFFER_SIZE, null)
                    IoUtil.close(objectResponse)
                })
        }
        return asyncGetResourceRegion(bucket, `object`, statObjectResponse, httpRanges)
    }

    override fun asyncDownloadSingleResource(
        bucket: String,
        `object`: String
    ): ResponseEntity<StreamingResponseBody> {
        val statObjectResponse: StatObjectResponse? = try {
            minioHelper.statObject(bucket, `object`)
        } catch (e: Exception) {
            log.error(
                "Exception occurred when looking for object. Exception message: {}",
                e.message
            )
            return ResponseEntity.notFound().build()
        }
        val objectResponse = minioHelper.getObject(bucket, `object`)
        if (statObjectResponse == null || objectResponse == null) {
            log.warn("StatObjectResponse not found! Bucket: {}, Object: {}", bucket, `object`)
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.ACCEPT_RANGES, ACCEPT_RANGES_VALUE)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.builder("attachment").filename(`object`).build().toString()
            )
            .contentLength(statObjectResponse.size())
            .contentType(MediaType.parseMediaType(statObjectResponse.contentType()))
            .body(StreamingResponseBody { outputStream: OutputStream? ->
                NioUtil.copyByNIO(objectResponse, outputStream, NioUtil.DEFAULT_LARGE_BUFFER_SIZE, null)
                IoUtil.close(objectResponse)
            })
    }

    override fun stateObject(
        bucket: String,
        `object`: String
    ): SerializableStatObjectResponse {
        val statObjectResponse = minioHelper.statObject(bucket, `object`)
        if (statObjectResponse == null) {
            log.warn("StatObjectResponse not found! Bucket: {}, Object: {}", bucket, `object`)
            throw ResourceNotFoundException("Object not found!")
        }
        return SerializableStatObjectResponse.build(statObjectResponse)
    }

    private fun asyncGetResourceRegion(
        bucket: String,
        `object`: String,
        statObjectResponse: StatObjectResponse,
        httpRanges: List<HttpRange>
    ): ResponseEntity<StreamingResponseBody> {
        val objectResponse = minioHelper.getObject(
            bucket,
            `object`,
            httpRanges[0].getRangeStart(0),
            LARGE_CHUNK_SIZE.toBytes()
        )
        if (objectResponse == null) {
            log.warn("Object not found! Bucket: $bucket, Object: $`object`")
            return ResponseEntity.notFound().build()
        }
        val start = httpRanges[0].getRangeStart(0)
        var end = start + LARGE_CHUNK_SIZE.toBytes() - 1
        val resourceLength = statObjectResponse.size()
        end = end.coerceAtMost(resourceLength - 1)
        val rangeLength = end - start + 1
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
            .header(HttpHeaders.ACCEPT_RANGES, ACCEPT_RANGES_VALUE)
            .header(HttpHeaders.CONTENT_RANGE, "bytes $start-$end/$resourceLength")
            .contentLength(rangeLength)
            .contentType(MediaType.parseMediaType(statObjectResponse.contentType()))
            .body(StreamingResponseBody { outputStream: OutputStream? ->
                NioUtil.copyByNIO(objectResponse, outputStream, NioUtil.DEFAULT_LARGE_BUFFER_SIZE, null)
                IoUtil.close(objectResponse)
            })
    }
}
