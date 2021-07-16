package com.jmsoftware.maf.springcloudstarter.configuration;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description: LocalDateTimeSerializerConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 3:57 PM
 **/
@RequiredArgsConstructor
public class JacksonConfiguration {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final JacksonProperties jacksonProperties;

    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        var pattern = DEFAULT_DATE_FORMAT;
        if (StrUtil.isNotBlank(jacksonProperties.getDateFormat())) {
            pattern = jacksonProperties.getDateFormat();
        }
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(LocalDateTimeSerializer localDateTimeSerializer) {
        var pattern = DEFAULT_DATE_FORMAT;
        if (StrUtil.isNotBlank(jacksonProperties.getDateFormat())) {
            pattern = jacksonProperties.getDateFormat();
        }
        val localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern));
        return jackson2ObjectMapperBuilder -> jackson2ObjectMapperBuilder
                .serializerByType(LocalDateTime.class, localDateTimeSerializer)
                .deserializerByType(LocalDateTime.class, localDateTimeDeserializer)
                .serializerByType(Long.class, ToStringSerializer.instance);
    }
}
