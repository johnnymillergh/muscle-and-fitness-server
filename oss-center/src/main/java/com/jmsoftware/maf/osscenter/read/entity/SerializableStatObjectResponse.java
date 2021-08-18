package com.jmsoftware.maf.osscenter.read.entity;

import io.minio.StatObjectResponse;
import io.minio.messages.LegalHold;
import io.minio.messages.RetentionMode;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Description: SerializableStatObjectResponse, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/12/2021 5:04 PM
 **/
@Data
@Setter(AccessLevel.NONE)
@EqualsAndHashCode(callSuper = true)
public class SerializableStatObjectResponse extends SerializableGenericResponse {
    private String etag;
    private long size;
    private ZonedDateTime lastModified;
    private RetentionMode retentionMode;
    private ZonedDateTime retentionRetainUntilDate;
    private LegalHold legalHold;
    private boolean deleteMarker;
    private Map<String, String> userMetadata;

    public static SerializableStatObjectResponse build(StatObjectResponse statObjectResponse) {
        val serializableStatObjectResponse = new SerializableStatObjectResponse();
        serializableStatObjectResponse.setHeaders(statObjectResponse.headers().toMultimap());
        serializableStatObjectResponse.setBucket(statObjectResponse.bucket());
        serializableStatObjectResponse.setRegion(statObjectResponse.region());
        serializableStatObjectResponse.setObject(statObjectResponse.object());
        serializableStatObjectResponse.etag = statObjectResponse.etag();
        serializableStatObjectResponse.size = statObjectResponse.size();
        serializableStatObjectResponse.lastModified = statObjectResponse.lastModified();
        serializableStatObjectResponse.retentionMode = statObjectResponse.retentionMode();
        serializableStatObjectResponse.retentionRetainUntilDate = statObjectResponse.retentionRetainUntilDate();
        serializableStatObjectResponse.legalHold = statObjectResponse.legalHold();
        serializableStatObjectResponse.deleteMarker = statObjectResponse.deleteMarker();
        serializableStatObjectResponse.userMetadata = statObjectResponse.userMetadata();
        return serializableStatObjectResponse;
    }
}
