package com.jmsoftware.maf.springbootstarter.configuration;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * <h1>CustomConfiguration</h1>
 * <p>Custom configurations which are written in .yml files, containing a variety of fragmentary configs. Such as,
 * Druid login info, web security switch, web log and so on.</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 14:24
 **/
@Data
@Validated
@Component
@ConfigurationProperties(prefix = "maf.configuration")
public class MafConfiguration {
    /**
     * <p>The username of super user who has no restriction to access any system&#39;s resources.</p>
     * <p><strong>ATTENTION</strong>: The value of username of super user must be equal to the value that is
     * persistent in database.</p>
     */
    @NotBlank
    private String superUser = "admin";
    /**
     * Ignore URLs, used by web access log filter and web security.
     */
    @Valid
    private IgnoredUrl ignoredUrl;
    /**
     * <p>Web security feature switch. Default is false.</p>
     * true - disable web security; false - enable web security.
     */
    @NotNull
    private Boolean webSecurityDisabled = false;
    /**
     * Web request log switch. Default is false.
     * <p>
     * true - disable web request log; false - enable web request log.
     */
    @NotNull
    private Boolean webRequestLogDisabled = false;
    /**
     * Included package for http api scan, could be base package
     */
    @NotBlank
    private String includedPackageForHttpApiScan;
    /**
     * Flatten ignored urls string [ ].
     *
     * @return the string [ ]
     */
    public String[] flattenIgnoredUrls() {
        if (ObjectUtil.isNull(ignoredUrl)) {
            return new String[0];
        }
        val flattenIgnoredUrls = new ArrayList<String>();
        flattenIgnoredUrls.addAll(ignoredUrl.getGet());
        flattenIgnoredUrls.addAll(ignoredUrl.getPost());
        flattenIgnoredUrls.addAll(ignoredUrl.getDelete());
        flattenIgnoredUrls.addAll(ignoredUrl.getPut());
        flattenIgnoredUrls.addAll(ignoredUrl.getHead());
        flattenIgnoredUrls.addAll(ignoredUrl.getPatch());
        flattenIgnoredUrls.addAll(ignoredUrl.getOptions());
        flattenIgnoredUrls.addAll(ignoredUrl.getTrace());
        flattenIgnoredUrls.addAll(ignoredUrl.getPattern());
        return flattenIgnoredUrls.toArray(new String[0]);
    }
}
