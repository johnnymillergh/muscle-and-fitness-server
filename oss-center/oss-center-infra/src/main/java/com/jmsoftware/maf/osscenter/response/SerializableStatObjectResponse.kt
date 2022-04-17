package com.jmsoftware.maf.osscenter.response

import io.minio.StatObjectResponse
import io.minio.messages.LegalHold
import io.minio.messages.RetentionMode
import java.time.ZonedDateTime

/**
 * # SerializableStatObjectResponse
 *
 * Description: SerializableStatObjectResponse, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/17/22 8:39 AM
 */
class SerializableStatObjectResponse : SerializableGenericResponse() {
    var etag: String? = null
    var size: Long = 0
    var lastModified: ZonedDateTime? = null
    var retentionMode: RetentionMode? = null
    var retentionRetainUntilDate: ZonedDateTime? = null
    var legalHold: LegalHold? = null
    var deleteMarker = false
    var userMetadata: Map<String, String>? = null

    companion object {
        fun build(statObjectResponse: StatObjectResponse): SerializableStatObjectResponse {
            // TODO: use MapStruct to convert
            val serializableStatObjectResponse = SerializableStatObjectResponse()
            serializableStatObjectResponse.headers = statObjectResponse.headers().toMultimap()
            serializableStatObjectResponse.bucket = statObjectResponse.bucket()
            serializableStatObjectResponse.region = statObjectResponse.region()
            serializableStatObjectResponse.`object` = statObjectResponse.`object`()
            serializableStatObjectResponse.etag = statObjectResponse.etag()
            serializableStatObjectResponse.size = statObjectResponse.size()
            serializableStatObjectResponse.lastModified = statObjectResponse.lastModified()
            serializableStatObjectResponse.retentionMode = statObjectResponse.retentionMode()
            serializableStatObjectResponse.retentionRetainUntilDate = statObjectResponse.retentionRetainUntilDate()
            serializableStatObjectResponse.legalHold = statObjectResponse.legalHold()
            serializableStatObjectResponse.deleteMarker = statObjectResponse.deleteMarker()
            serializableStatObjectResponse.userMetadata = statObjectResponse.userMetadata()
            return serializableStatObjectResponse
        }
    }
}
