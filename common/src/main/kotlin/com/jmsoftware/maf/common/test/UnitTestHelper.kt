package com.jmsoftware.maf.common.test

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.jmsoftware.maf.common.test.UnitTestHelper.Companion.OBJECT_MAPPER
import com.jmsoftware.maf.common.test.UnitTestHelper.Companion.log
import com.jmsoftware.maf.common.util.logger

/**
 * # UnitTestHelper
 *
 * change description here.
 *
 * @author Johnny Miller, email: johnnysviva@outlook.com, date: 7/25/2023 8:52 PM
 **/
class UnitTestHelper {
    companion object {
        val log = logger()
        val OBJECT_MAPPER: ObjectMapper = ObjectMapper().registerModules(
            JavaTimeModule(),
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        ).also {
            it.setSerializationInclusion(NON_NULL)
        }
    }
}

/**
 * Parse JSON string to an object.
 *
 * @param T the type of the object
 * @param context the context object, which could be an instance of KClass, or `this` when calling this method
 * @param jsonPath the path to the JSON file under `resources` directory
 * @return the object deserialized from the JSON file
 */
inline fun <reified T : Any> parseJson(context: Any, jsonPath: String): T {
    return context.javaClass.classLoader.getResourceAsStream(jsonPath).use {
        if (it == null) {
            log.warn("Could not find the resource: $jsonPath")
            throw IllegalArgumentException("Could not find the resource: $jsonPath")
        }
        OBJECT_MAPPER.readValue<T>(it.readBytes())
    }
}

/**
 * Stringify an object to a JSON string.
 *
 * @param any the object
 * @return the JSON string
 */
fun jsonStringify(any: Any): String = OBJECT_MAPPER.writeValueAsString(any)
