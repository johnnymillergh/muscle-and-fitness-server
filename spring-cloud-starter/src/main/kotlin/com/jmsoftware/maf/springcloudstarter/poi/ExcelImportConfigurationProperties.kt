package com.jmsoftware.maf.springcloudstarter.poi

import com.jmsoftware.maf.common.util.logger
import jakarta.annotation.PostConstruct
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

/**
 * # ExcelImportConfigurationProperties
 *
 * Custom configurations which are written in .yml files, containing a variety of fragmentary configs. Such as,
 * Druid login info, web security switch, web log and so on.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 3:58 PM
 */
@Validated
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = ExcelImportConfigurationProperties.PREFIX)
class ExcelImportConfigurationProperties {
    companion object {
        const val PREFIX = "maf.configuration.excel"
        private val log = logger()
    }

    @Min(2)
    @NotNull
    var maximumRowCount: Int = 2

    @PostConstruct
    private fun postConstruct() {
        log.warn("Initial bean: `${this.javaClass.simpleName}`")
    }
}
