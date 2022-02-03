package com.jmsoftware.maf.springcloudstarter.property;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>MafConfigurationProperties</h1>
 * <p>MAF Configuration which are written in .yml files, containing a variety of fragmentary configs. Such as,
 * Druid login info, web security switch, web log and so on.</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 14:24
 **/
@Data
@Slf4j
@Validated
@ConfigurationProperties(prefix = "maf.configuration")
public class MafConfigurationProperties {
    /**
     * <p>The role name of super user (admin) who has no restriction to access any system&#39;s resources.</p>
     * <p><strong>ATTENTION</strong>: The value of role name of super user must be equal to the value that is
     * persistent in database.</p>
     */
    @NotBlank
    private String superUserRole = "admin";
    /**
     * <p>The role name of guest user (guest)  who has restrictions to access any system&#39;s resources. Assigning
     * the guest role to new user by default.</p>
     * <p><strong>ATTENTION</strong>: The value of role of guest user must be equal to the value that is
     * persistent in database.</p>
     */
    @NotBlank
    private String guestUserRole = "guest";
    /**
     * Ignore URLs, used by web access log filter and web security.
     */
    @Valid
    private IgnoredUrl ignoredUrl;
    /**
     * <p>Web security feature switch. Default is true.</p>
     * true - disable web security; false - enable web security.
     */
    @NotNull
    private Boolean webSecurityEnabled = Boolean.TRUE;
    /**
     * Web request log switch. Default is true.
     * <p>
     * true - disable web request log; false - enable web request log.
     */
    @NotNull
    private Boolean webRequestLogEnabled = Boolean.TRUE;
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
        if (ObjectUtil.isNull(this.ignoredUrl)) {
            return new String[0];
        }
        val flattenIgnoredUrls = new ArrayList<String>();
        flattenIgnoredUrls.addAll(this.ignoredUrl.getGet());
        flattenIgnoredUrls.addAll(this.ignoredUrl.getPost());
        flattenIgnoredUrls.addAll(this.ignoredUrl.getDelete());
        flattenIgnoredUrls.addAll(this.ignoredUrl.getPut());
        flattenIgnoredUrls.addAll(this.ignoredUrl.getHead());
        flattenIgnoredUrls.addAll(this.ignoredUrl.getPatch());
        flattenIgnoredUrls.addAll(this.ignoredUrl.getOptions());
        flattenIgnoredUrls.addAll(this.ignoredUrl.getTrace());
        flattenIgnoredUrls.addAll(this.ignoredUrl.getPattern());
        return flattenIgnoredUrls.toArray(new String[0]);
    }

    @PostConstruct
    private void postConstruct() {
        log.warn("Initial bean: '{}'", this.getClass().getSimpleName());
    }

    /**
     * <h1>IgnoredUrl</h1>
     * <p>
     * Ignored URL configuration.
     *
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 2/9/2021 9:39 AM
     **/
    @Data
    public static class IgnoredUrl {
        private interface Constant {
            String URL_REGEXP = "^(/.+)+$";
        }

        /**
         * Ignored URL pattern.
         */
        @Valid
        private List<@Pattern(regexp = Constant.URL_REGEXP) String> pattern = Lists.newArrayList();
        /**
         * Ignored GET request.
         */
        @Valid
        private List<@Pattern(regexp = Constant.URL_REGEXP) String> get = Lists.newArrayList();
        /**
         * Ignored POST request.
         */
        @Valid
        private List<@Pattern(regexp = Constant.URL_REGEXP) String> post = Lists.newArrayList();
        /**
         * Ignored DELETE request.
         */
        @Valid
        private List<@Pattern(regexp = Constant.URL_REGEXP) String> delete = Lists.newArrayList();
        /**
         * Ignored PUT request.
         */
        @Valid
        private List<@Pattern(regexp = Constant.URL_REGEXP) String> put = Lists.newArrayList();
        /**
         * Ignored HEAD request.
         */
        @Valid
        private List<@Pattern(regexp = Constant.URL_REGEXP) String> head = Lists.newArrayList();
        /**
         * Ignored PATCH request.
         */
        @Valid
        private List<@Pattern(regexp = Constant.URL_REGEXP) String> patch = Lists.newArrayList();
        /**
         * Ignored OPTIONS request.
         */
        @Valid
        private List<@Pattern(regexp = Constant.URL_REGEXP) String> options = Lists.newArrayList();
        /**
         * Ignored TRACE request.
         */
        @Valid
        private List<@Pattern(regexp = Constant.URL_REGEXP) String> trace = Lists.newArrayList();
    }
}
