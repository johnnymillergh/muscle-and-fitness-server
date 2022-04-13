package com.jmsoftware.maf.springcloudstarter.property

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties.IgnoredUrl.Constant.Companion.URL_REGEXP
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.annotation.PostConstruct
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

/**
 * # MafConfigurationProperties
 *
 * MAF Configuration which are written in .yml files, containing a variety of fragmentary configs. Such as,
 * Druid login info, web security switch, web log and so on.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/13/22 2:07 PM
 **/
@Validated
@Suppress("unused")
@ConfigurationProperties(prefix = MafConfigurationProperties.PREFIX)
class MafConfigurationProperties {
    companion object {
        const val PREFIX = "maf.configuration"
        private val log = logger()
    }

    /**
     * The role name of superuser (admin) who has no restriction to access any system's resources.
     *
     * **ATTENTION**: The value of role name of superuser must be equal to the value that is persistent in database.
     **/
    @NotBlank
    var superUserRole: String = "admin"

    /**
     * The role name of guest user (guest)  who has restrictions to access any system&#39;s resources. Assigning
     * the guest role to new user by default.
     *
     * **ATTENTION**: The value of role of guest user must be equal to the value that is persistent in database.
     **/
    @NotBlank
    var guestUserRole: String = "guest"

    /**
     * Ignore URLs, used by web access log filter and web security.
     **/
    @Valid
    var ignoredUrl: IgnoredUrl? = null

    /**
     * Web security feature switch. Default is true.
     * true - disable web security; false - enable web security.
     **/
    var webSecurityEnabled: Boolean = true

    /**
     * Web request log switch. Default is true.
     *
     * true - disable web request log; false - enable web request log.
     **/
    var webRequestLogEnabled: Boolean = true

    /**
     * Included package for http api scan, could be base package
     **/
    @NotBlank
    var includedPackageForHttpApiScan: String = ""

    @PostConstruct
    private fun postConstruct() {
        log.warn("Initial bean: `${this.javaClass.simpleName}`")
    }

    /**
     * Flatten ignored urls string [ ].
     *
     * @return the string [ ]
     **/
    fun flattenIgnoredUrls(): List<String> {
        return mutableListOf<String>().let {
            it.addAll(ignoredUrl?.get!!)
            it.addAll(ignoredUrl?.post!!)
            it.addAll(ignoredUrl?.delete!!)
            it.addAll(ignoredUrl?.put!!)
            it.addAll(ignoredUrl?.head!!)
            it.addAll(ignoredUrl?.patch!!)
            it.addAll(ignoredUrl?.options!!)
            it.addAll(ignoredUrl?.trace!!)
            it.addAll(ignoredUrl?.pattern!!)
            it
        }
    }

    /**
     * <h1>IgnoredUrl</h1>
     *
     *
     * Ignored URL configuration.
     *
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 2/9/2021 9:39 AM
     **/
    class IgnoredUrl {
        private interface Constant {
            companion object {
                const val URL_REGEXP = "^(/.+)+$"
            }
        }

        /**
         * Ignored URL pattern.
         **/
        @Valid
        val pattern: MutableList<@Pattern(regexp = URL_REGEXP) String> = mutableListOf()

        /**
         * Ignored GET request.
         **/
        @Valid
        val get: MutableList<@Pattern(regexp = URL_REGEXP) String> = mutableListOf()

        /**
         * Ignored POST request.
         **/
        @Valid
        val post: MutableList<@Pattern(regexp = URL_REGEXP) String> = mutableListOf()

        /**
         * Ignored DELETE request.
         **/
        @Valid
        val delete: MutableList<@Pattern(regexp = URL_REGEXP) String> = mutableListOf()

        /**
         * Ignored PUT request.
         **/
        @Valid
        val put: MutableList<@Pattern(regexp = URL_REGEXP) String> = mutableListOf()

        /**
         * Ignored HEAD request.
         **/
        @Valid
        val head: MutableList<@Pattern(regexp = URL_REGEXP) String> = mutableListOf()

        /**
         * Ignored PATCH request.
         **/
        @Valid
        val patch: MutableList<@Pattern(regexp = URL_REGEXP) String> = mutableListOf()

        /**
         * Ignored OPTIONS request.
         **/
        @Valid
        val options: MutableList<@Pattern(regexp = URL_REGEXP) String> = mutableListOf()

        /**
         * Ignored TRACE request.
         **/
        @Valid
        val trace: MutableList<@Pattern(regexp = URL_REGEXP) String> = mutableListOf()
    }
}
