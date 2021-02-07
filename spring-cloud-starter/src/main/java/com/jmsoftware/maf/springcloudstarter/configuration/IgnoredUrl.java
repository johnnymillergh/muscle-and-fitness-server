package com.jmsoftware.maf.springcloudstarter.configuration;

import com.google.common.collect.Lists;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * <h1>IgnoredUrl</h1>
 * <p>
 * Ignored URL configuration.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/2/20 11:41 PM
 **/
@Data
public class IgnoredUrl {
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
