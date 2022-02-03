package com.jmsoftware.maf.osscenter.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse;
import com.jmsoftware.maf.osscenter.payload.MergeResourceChunkPayload;
import com.jmsoftware.maf.osscenter.payload.UploadResourceChunkPayload;
import com.jmsoftware.maf.osscenter.service.WriteResourceService;
import com.jmsoftware.maf.springcloudstarter.minio.MinioHelper;
import io.minio.ComposeSource;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.jmsoftware.maf.osscenter.constant.Chunk.TINY_CHUNK_SIZE;

/**
 * <h1>WriteResourceServiceImpl</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 2:20 PM
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class WriteResourceServiceImpl implements WriteResourceService {
    private final MinioHelper minioHelper;

    @Override
    @SneakyThrows
    public ObjectResponse uploadSingleResource(@NotNull MultipartFile multipartFile) {
        val mediaType = this.parseMediaType(multipartFile);
        this.minioHelper.makeBucket(mediaType.getType());
        val objectWriteResponse = this.minioHelper.put(mediaType.getType(), multipartFile.getOriginalFilename(),
                                                       multipartFile);
        val objectResponse = new ObjectResponse();
        objectResponse.setBucket(objectWriteResponse.bucket());
        objectResponse.setObject(objectWriteResponse.object());
        objectResponse.setEtag(objectWriteResponse.etag());
        log.info("Uploaded single resource. {}", objectResponse);
        return objectResponse;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <h2 >What is an S3 ETag?</h2>
     * <p>According to
     * <a href='https://docs.aws.amazon.com/AmazonS3/latest/API/RESTCommonResponseHeaders.html'>Amazon</a>:</p>
     * <blockquote><p>The ETag may or may not be an MD5 digest of the object data</p>
     * </blockquote>
     * <p>Basically, if the object was uploaded with a single PUT operation and doesnt use Customer Managed or KMS
     * keys for encryption then the resulting ETag is just the MD5 hexdigest of the object.</p>
     * <p>However, more importantly:</p>
     * <blockquote><p>If an object is created by either the Multipart Upload or Part Copy operation, the ETag is not
     * an MD5 digest, regardless of the method of encryption.</p>
     * </blockquote>
     * <p>Well if it’s not an MD5 digest then what is it?!</p>
     * <blockquote><p>For multipart uploads the ETag is the MD5 hexdigest of each part’s MD5 digest concatenated
     * together, followed by the number of parts separated by a dash.</p>
     * </blockquote>
     * <p>E.g. for a two part object the ETag may look something like this:</p>
     * <blockquote><p>d41d8cd98f00b204e9800998ecf8427e-2</p>
     * </blockquote>
     * <p>Which can be represented by:</p>
     * <blockquote><p>hexmd5( md5( part1 ) + md5( part2 ) )-{ number of parts }</p>
     * </blockquote>
     *
     * @see <a href='https://teppen.io/2018/06/23/aws_s3_etags/'>All about AWS S3 ETags</a>
     */
    @Override
    @SneakyThrows
    public ObjectResponse uploadResourceChunk(
            @NotNull MultipartFile multipartFile,
            @Valid @NotNull UploadResourceChunkPayload payload
    ) {
        if (CharSequenceUtil.isBlank(multipartFile.getOriginalFilename())) {
            throw new IllegalArgumentException("File name required");
        }
        MediaType mediaType = null;
        if (CharSequenceUtil.isBlank(payload.getBucket())) {
            mediaType = this.parseMediaType(multipartFile);
        }
        // bucketName is either mediaType of given 'bucket'
        val bucketName = CharSequenceUtil.isBlank(payload.getBucket()) ?
                Objects.requireNonNull(mediaType).getType() : payload.getBucket();
        val orderedFilename = String.format("%s.chunk%s", payload.getFilename(),
                                            NumberUtil.decimalFormat("000", payload.getChunkNumber()));
        val objectResponse = new ObjectResponse();
        objectResponse.setBucket(bucketName);
        objectResponse.setObject(orderedFilename);
        this.minioHelper.makeBucket(bucketName);
        val objectWriteResponse = this.minioHelper.put(bucketName, orderedFilename, multipartFile);
        objectResponse.setEtag(objectWriteResponse.etag());
        log.info("Uploaded resource chunk. {}", objectResponse);
        return objectResponse;
    }

    @Override
    @SneakyThrows
    public ObjectResponse mergeResourceChunk(@Valid @NotNull MergeResourceChunkPayload payload) {
        val objectName = this.validateObject(payload.getObjectList());
        val sources = payload.getObjectList()
                .stream()
                .map(object -> ComposeSource.builder().bucket(payload.getBucket()).object(object).build())
                .collect(Collectors.toList());
        @Cleanup val firstChunk = this.minioHelper.getObject(payload.getBucket(),
                                                             CollUtil.getFirst(payload.getObjectList()),
                                                             0,
                                                             TINY_CHUNK_SIZE.toBytes());
        val mediaType = this.parseMediaType(firstChunk);
        val headers = new HashMap<String, String>(4);
        headers.put("Content-Type", mediaType.toString());
        val objectWriteResponse = this.minioHelper.composeObject(payload.getBucket(), objectName, sources, headers);
        val objectResponse = new ObjectResponse();
        objectResponse.setBucket(objectWriteResponse.bucket());
        objectResponse.setObject(objectWriteResponse.object());
        objectResponse.setEtag(objectWriteResponse.etag());
        log.info("Merged resource chunks. {}", objectResponse);
        val errorObjectList = this.minioHelper.removeObjects(payload.getBucket(), payload.getObjectList());
        log.warn("Removed unnecessary objects. errorObjectList: {}", errorObjectList);
        return objectResponse;
    }

    private String validateObject(List<String> objectList) {
        val objectNameSet = objectList.stream().map(object -> {
            val lastIndexOfDot = CharSequenceUtil.lastIndexOfIgnoreCase(object, ".");
            return CharSequenceUtil.subPre(object, lastIndexOfDot);
        }).collect(Collectors.toSet());
        if (CollUtil.size(objectNameSet) != 1) {
            log.error("Object list is not valid! {}", objectNameSet);
            throw new IllegalArgumentException("Object list is not valid");
        }
        return objectNameSet.iterator().next();
    }

    private MediaType parseMediaType(MultipartFile multipartFile) throws IOException {
        val tika = new Tika();
        val detectedMediaType = tika.detect(multipartFile.getInputStream());
        log.info("Detected media type: {}", detectedMediaType);
        if (CharSequenceUtil.isBlank(detectedMediaType)) {
            throw new IllegalStateException("Media extension detection failed!");
        }
        return MediaType.parse(detectedMediaType);
    }

    private MediaType parseMediaType(InputStream inputStream) throws IOException {
        val tika = new Tika();
        val detectedMediaType = tika.detect(inputStream);
        log.info("Detected media type: {}", detectedMediaType);
        if (CharSequenceUtil.isBlank(detectedMediaType)) {
            throw new IllegalStateException("Media extension detection failed!");
        }
        return MediaType.parse(detectedMediaType);
    }
}
