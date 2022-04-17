package com.jmsoftware.maf.osscenter.response

/**
 * # SerializableGenericResponse
 * 
 * Description: SerializableGenericResponse, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/17/22 8:38 AM
 */
open class SerializableGenericResponse {
    var headers: Map<String, List<String>>? = null
    var bucket: String? = null
    var region: String? = null
    var `object`: String? = null
}
