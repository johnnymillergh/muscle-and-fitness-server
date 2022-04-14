package com.jmsoftware.maf.springcloudstarter.configuration

import cn.hutool.core.util.StrUtil
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.jmsoftware.maf.common.constant.UniversalDateTime
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.autoconfigure.jackson.JacksonProperties
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * # JacksonConfiguration
 *
 * Description: JacksonConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:33 AM
 */
class JacksonConfiguration(
    private val jacksonProperties: JacksonProperties
) {
    @Bean
    fun localDateTimeSerializer(): LocalDateTimeSerializer {
        var pattern: String = UniversalDateTime.DATE_TIME_FORMAT
        if (StrUtil.isNotBlank(jacksonProperties.dateFormat)) {
            pattern = jacksonProperties.dateFormat
        }
        return LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern))
    }

    @Bean
    fun jackson2ObjectMapperBuilderCustomizer(localDateTimeSerializer: LocalDateTimeSerializer): Jackson2ObjectMapperBuilderCustomizer {
        var pattern: String? = UniversalDateTime.DATE_TIME_FORMAT
        if (StrUtil.isNotBlank(jacksonProperties.dateFormat)) {
            pattern = jacksonProperties.dateFormat
        }
        val localDateTimeDeserializer = LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern))
        return Jackson2ObjectMapperBuilderCustomizer { jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder ->
            jackson2ObjectMapperBuilder
                .serializerByType(LocalDateTime::class.java, localDateTimeSerializer)
                .deserializerByType(LocalDateTime::class.java, localDateTimeDeserializer)
                .serializerByType(Long::class.java, ToStringSerializer.instance)
        }
    }
}
