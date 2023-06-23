package com.jmsoftware.maf.reactivespringcloudstarter.property

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafConfigurationProperties.IgnoredUrl.Constant.Companion.URL_REGEXP
import jakarta.annotation.PostConstruct
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

/**
 * # MafConfigurationProperties
 *
 * MAF Configuration which are written in .yml files, containing a variety of fragmentary configs. Such as,
 * Druid login info, web security switch, web log and so on.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/13/22 2:07 PM
 **/
@Validated
@Configuration
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
     * # IgnoredUrl
     *
     * Ignored URL configuration.
     *
     * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/18/22 9:19 PM
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
