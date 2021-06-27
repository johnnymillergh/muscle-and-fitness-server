package com.jmsoftware.maf.springcloudstarter.configuration;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.springcloudstarter.constant.UniversalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Description: TypeConvesionConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 9:36 PM
 **/
@Slf4j
public class TypeConversionConfiguration {
    @Bean
    public StringToLocalDateConverter localDateConverter() {
        return source -> {
            if (StrUtil.isBlank(source)) {
                return null;
            }
            return LocalDateTimeUtil.parseDate(source, UniversalDateTime.DATE_FORMAT);
        };
    }

    @Bean
    public StringToLocalTimeConverter localTimeConverter() {
        return source -> {
            if (StrUtil.isBlank(source)) {
                return null;
            }
            return LocalTime.parse(source, DateTimeFormatter.ofPattern(UniversalDateTime.TIME_FORMAT));
        };
    }

    @Bean
    @ConditionalOnBean(name = "requestMappingHandlerAdapter")
    public StringToLocalDateTimeConverter localDateTimeConverter() {
        return source -> {
            if (StrUtil.isBlank(source)) {
                return null;
            }
            return LocalDateTimeUtil.parse(source, UniversalDateTime.DATE_TIME_FORMAT);
        };
    }

    interface StringToLocalDateConverter extends Converter<String, LocalDate> {
    }

    interface StringToLocalTimeConverter extends Converter<String, LocalTime> {
    }

    interface StringToLocalDateTimeConverter extends Converter<String, LocalDateTime> {
    }
}
