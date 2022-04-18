package com.jmsoftware.maf.osscenter.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.text.CharSequenceUtil
import cn.hutool.core.util.NumberUtil
import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.osscenter.constant.Chunk
import com.jmsoftware.maf.osscenter.payload.MergeResourceChunkPayload
import com.jmsoftware.maf.osscenter.payload.UploadResourceChunkPayload
import com.jmsoftware.maf.osscenter.service.WriteResourceService
import com.jmsoftware.maf.springcloudstarter.minio.MinioHelper
import io.minio.ComposeSource
import lombok.SneakyThrows
import org.apache.tika.Tika
import org.apache.tika.mime.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.util.stream.Collectors

/**
 * # WriteResourceServiceImpl
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:48 PM
 */
@Service
class WriteResourceServiceImpl(
    private val minioHelper: MinioHelper
) : WriteResourceService {
    companion object {
        private val log = logger()
    }

    override fun uploadSingleResource(multipartFile: MultipartFile): ObjectResponse {
        val mediaType = this.parseMediaType(multipartFile)
        minioHelper.makeBucket(mediaType.type)
        val objectWriteResponse = minioHelper.put(
            mediaType.type,
            multipartFile.originalFilename!!,
            multipartFile
        )
        val objectResponse = ObjectResponse()
        objectResponse.bucket = objectWriteResponse.bucket()
        objectResponse.`object` = objectWriteResponse.`object`()
        objectResponse.etag = objectWriteResponse.etag()
        log.info("Uploaded single resource. {}", objectResponse)
        return objectResponse
    }

    /**
     * ## What is an S3 ETag?
     *
     * According to [Amazon](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTCommonResponseHeaders.html):
     *
     * > The ETag may or may not be an MD5 digest of the object data
     *
     * Basically, if the object was uploaded with a single PUT operation and doesnt use Customer Managed or KMS keys for encryption then the resulting ETag is just the MD5 hexdigest of the object.
     *
     * However, more importantly:
     *
     * > If an object is created by either the Multipart Upload or Part Copy operation, the ETag is not an MD5 digest, regardless of the method of encryption.
     *
     * Well if it’s not an MD5 digest then what is it?!
     *
     * > For multipart uploads the ETag is the MD5 hexdigest of each part’s MD5 digest concatenated together, followed by the number of parts separated by a dash.
     *
     * E.g. for a two part object the ETag may look something like this:
     *
     * > d41d8cd98f00b204e9800998ecf8427e-2
     *
     * Which can be represented by:
     *
     * > hexmd5( md5( part1 ) + md5( part2 ) )-{ number of parts }
     *
     * E.g. for a two part object the ETag may look something like this:
     *
     * > d41d8cd98f00b204e9800998ecf8427e-2
     *
     * Which can be represented by:
     *
     * > hexmd5( md5( part1 ) + md5( part2 ) )-{ number of parts }
     *
     * @see <a href='https://teppen.io/2018/06/23/aws_s3_etags/'>All about AWS S3 ETags</a>
     */
    override fun uploadResourceChunk(
        multipartFile: MultipartFile,
        payload: UploadResourceChunkPayload
    ): ObjectResponse {
        require(!CharSequenceUtil.isBlank(multipartFile.originalFilename)) { "File name required" }
        var mediaType: MediaType? = null
        if (CharSequenceUtil.isBlank(payload.bucket)) {
            mediaType = this.parseMediaType(multipartFile)
        }
        // bucketName is either mediaType of given 'bucket'
        val bucketName =
            if (CharSequenceUtil.isBlank(payload.bucket)) mediaType!!.type else payload.bucket
        val orderedFilename = CharSequenceUtil.format(
            "{}.chunk{}", payload.filename,
            NumberUtil.decimalFormat("000", payload.chunkNumber)
        )
        val objectResponse = ObjectResponse()
        objectResponse.bucket = bucketName
        objectResponse.`object` = orderedFilename
        minioHelper.makeBucket(bucketName)
        val objectWriteResponse = minioHelper.put(bucketName!!, orderedFilename, multipartFile)
        objectResponse.etag = objectWriteResponse.etag()
        log.info("Uploaded resource chunk. $objectResponse")
        return objectResponse
    }

    @SneakyThrows
    override fun mergeResourceChunk(payload: MergeResourceChunkPayload): ObjectResponse {
        val objectName = validateObject(payload.objectList)
        val sources = payload.objectList
            .stream()
            .map { `object`: String ->
                ComposeSource.builder().bucket(
                    payload.bucket
                ).`object`(`object`).build()
            }
            .collect(Collectors.toList())
        val mediaType = minioHelper.getObject(
            payload.bucket,
            CollUtil.getFirst(payload.objectList),
            0,
            Chunk.TINY_CHUNK_SIZE.toBytes()
        )!!.use {
            this.parseMediaType(it)
        }
        val headers = HashMap<String, String>(4)
        headers["Content-Type"] = mediaType.toString()
        val objectWriteResponse = minioHelper.composeObject(payload.bucket, objectName, sources, headers)
        if (objectWriteResponse == null) {
            log.error("objectWriteResponse is null!")
            throw IllegalStateException("Failed to compose object!")
        }
        val objectResponse = ObjectResponse()
        objectResponse.bucket = objectWriteResponse.bucket()
        objectResponse.`object` = objectWriteResponse.`object`()
        objectResponse.etag = objectWriteResponse.etag()
        log.info("Merged resource chunks. {}", objectResponse)
        val errorObjectList = minioHelper.removeObjects(payload.bucket, payload.objectList)
        log.warn("Removed unnecessary objects. errorObjectList: $errorObjectList")
        return objectResponse
    }

    private fun validateObject(objectList: List<String>): String {
        val objectNameSet = objectList.stream().map { `object`: String? ->
            val lastIndexOfDot = CharSequenceUtil.lastIndexOfIgnoreCase(`object`, ".")
            CharSequenceUtil.subPre(`object`, lastIndexOfDot)
        }.collect(Collectors.toSet())
        if (CollUtil.size(objectNameSet) != 1) {
            log.error("Object list is not valid! {}", objectNameSet)
            throw IllegalArgumentException("Object list is not valid")
        }
        return objectNameSet.iterator().next()
    }

    private fun parseMediaType(multipartFile: MultipartFile): MediaType {
        val tika = Tika()
        val detectedMediaType = tika.detect(multipartFile.inputStream)
        log.info("Detected media type: $detectedMediaType")
        check(detectedMediaType.isNotBlank()) { "Media extension detection failed!" }
        return MediaType.parse(detectedMediaType)
    }

    private fun parseMediaType(inputStream: InputStream): MediaType {
        val tika = Tika()
        val detectedMediaType = tika.detect(inputStream)
        log.info("Detected media type: $detectedMediaType")
        check(detectedMediaType.isNotBlank()) { "Media extension detection failed!" }
        return MediaType.parse(detectedMediaType)
    }
}
