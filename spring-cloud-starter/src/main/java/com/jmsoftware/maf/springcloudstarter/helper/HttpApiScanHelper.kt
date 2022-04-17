package com.jmsoftware.maf.springcloudstarter.helper

import com.jmsoftware.maf.common.util.logger
import org.springframework.validation.annotation.Validated
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import javax.validation.constraints.NotBlank

/**
 * # HttpApiScanHelper
 *
 * Description: HttpApiScanHelper, scanning HTTP API for microservice.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 10:12 PM
 */
@Validated
class HttpApiScanHelper(
    private val requestMappingHandlerMapping: RequestMappingHandlerMapping
) {
    companion object {
        private var EXCLUDED_PACKAGE: String
        private val log = logger()

        init {
            val packagePaths = HttpApiScanHelper::class.java.getPackage().name.split(".")
            // Remove the last package name
            val jointPackage = buildString {
                for (index in 0 until packagePaths.size - 1) {
                    append("${packagePaths[index]}.")
                }
            }
            // Remove the last '.'
            EXCLUDED_PACKAGE = jointPackage.substring(0, jointPackage.length - 1)
            log.warn("EXCLUDED_PACKAGE was generated dynamically. EXCLUDED_PACKAGE = $EXCLUDED_PACKAGE")
        }
    }

    /**
     * Scan map.
     *
     * @param includedPackage the included package
     * @return the map
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/25/2020 4:02 PM
     */
    fun scan(@NotBlank includedPackage: String): Map<RequestMappingInfo, HandlerMethod> {
        val handlerMethods = requestMappingHandlerMapping.handlerMethods
        log.debug("Scanned request mapping info: {}", handlerMethods)
        val filteredHandlerMethods = LinkedHashMap<RequestMappingInfo, HandlerMethod>()
        handlerMethods.forEach { (requestMappingInfo: RequestMappingInfo, handlerMethod: HandlerMethod) ->
            // filter the Controller APIs that are defined in `spring-cloud-starter`
            if (handlerMethod.toString().contains(includedPackage)
                && !handlerMethod.toString().contains(EXCLUDED_PACKAGE)) {
                try {
                    val requestMethod = listOf(requestMappingInfo.methodsCondition.methods)[0]
                    log.debug(
                        "Request: [{}] {}, handler method: {}", requestMethod,
                        requestMappingInfo.patternsCondition, handlerMethod
                    )
                    filteredHandlerMethods[requestMappingInfo] = handlerMethod
                } catch (e: Exception) {
                    log.warn(
                        "Exception occurred when getting request method. Exception message: {}. Request mapping " +
                                "info: {}",
                        e.message, requestMappingInfo
                    )
                }
            }
        }
        return filteredHandlerMethods
    }
}
