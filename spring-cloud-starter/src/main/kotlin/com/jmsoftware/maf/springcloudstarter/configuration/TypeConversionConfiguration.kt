package com.jmsoftware.maf.springcloudstarter.configuration

import cn.hutool.core.date.LocalDateTimeUtil
import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.common.constant.UniversalDateTime
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.core.convert.converter.Converter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * # TypeConversionConfiguration
 *
 * Description: TypeConversionConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:58 AM
 */
class TypeConversionConfiguration {
    interface StringToLocalDateConverter : Converter<String?, LocalDate?>
    interface StringToLocalTimeConverter : Converter<String?, LocalTime?>
    interface StringToLocalDateTimeConverter : Converter<String?, LocalDateTime?>

    @Bean
    fun localDateConverter(): StringToLocalDateConverter {
        return object : StringToLocalDateConverter {
            override fun convert(source: String): LocalDate? {
                if (StrUtil.isBlank(source)) {
                    return null
                }
                return LocalDateTimeUtil.parseDate(source, UniversalDateTime.DATE_FORMAT)
            }
        }
    }

    @Bean
    fun localTimeConverter(): StringToLocalTimeConverter {
        return object : StringToLocalTimeConverter {
            override fun convert(source: String): LocalTime? {
                if (StrUtil.isBlank(source)) {
                    return null
                }
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(UniversalDateTime.TIME_FORMAT))
            }
        }
    }

    @Bean
    @ConditionalOnBean(name = ["requestMappingHandlerAdapter"])
    fun localDateTimeConverter(): StringToLocalDateTimeConverter {
        return object : StringToLocalDateTimeConverter {
            override fun convert(source: String): LocalDateTime? {
                if (StrUtil.isBlank(source)) {
                    return null
                }
                return LocalDateTimeUtil.parse(source, UniversalDateTime.DATE_TIME_FORMAT)
            }
        }
    }
}
