package com.jmsoftware.maf.springcloudstarter.configuration;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>CustomConfiguration</h1>
 * <p>Custom configurations which are written in .yml files, containing a variety of fragmentary configs. Such as,
 * Druid login info, web security switch, web log and so on.</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 14:24
 **/
@Data
@Slf4j
@Validated
@Component
@RefreshScope
@ConfigurationProperties(prefix = "maf.configuration.excel")
public class ExcelImportConfiguration {
    @Min(2)
    @NotNull
    private Integer maximumRowCount;

    @PostConstruct
    private void postConstruct() {
        log.warn("Initial bean: '{}'", this.getClass().getSimpleName());
    }
}
