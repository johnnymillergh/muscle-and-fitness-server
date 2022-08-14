package com.jmsoftware.maf.springcloudstarter.configuration

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer
import com.jmsoftware.maf.common.util.logger
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime

/**
 * # JacksonConfiguration
 *
 * Description: JacksonConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:33 AM
 */
class JacksonConfiguration {
    companion object {
        private val log = logger()
    }

    @Bean
    fun jackson2ObjectMapperBuilderCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
        return Jackson2ObjectMapperBuilderCustomizer { jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder ->
            jackson2ObjectMapperBuilder
                // Serialization & deserialization of `LocalDateTime`
                .serializerByType(LocalDateTime::class.java, LocalDateTimeSerializer.INSTANCE)
                .deserializerByType(LocalDateTime::class.java, LocalDateTimeDeserializer.INSTANCE)
                // Serialization of `ZonedDateTime`
                .serializerByType(ZonedDateTime::class.java, ZonedDateTimeSerializer.INSTANCE)
                // Serialization of `OffsetDateTime`
                .serializerByType(OffsetDateTime::class.java, OffsetDateTimeSerializer.INSTANCE)
                // Serialization of `Long`
                .serializerByType(Long::class.java, ToStringSerializer.instance)
        }.also {
            log.info("Customized Jackson ObjectMapper")
        }
    }
}
