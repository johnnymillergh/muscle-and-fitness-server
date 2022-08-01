package com.jmsoftware.maf.springcloudstarter.controller

import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse
import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse.HttpApiResource
import com.jmsoftware.maf.springcloudstarter.helper.HttpApiScanHelper
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import java.util.*

/**
 * # HttpApiResourceRemoteApiController
 *
 * Description: HttpApiResourceRemoteApiController, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/13/22 1:45 PM
 */
@RestController
class HttpApiResourceRemoteApiController(
    private val mafConfigurationProperties: MafConfigurationProperties,
    private val httpApiScanHelper: HttpApiScanHelper
) {
    @GetMapping("/http-api-resources")
    fun httpApiResource(): ResponseBodyBean<HttpApiResourcesResponse> {
        val handlerMethodMap = httpApiScanHelper.scan(mafConfigurationProperties.includedPackageForHttpApiScan)
        val response = HttpApiResourcesResponse()
        handlerMethodMap.forEach { (requestMappingInfo: RequestMappingInfo, _: HandlerMethod) ->
            val requestMethod = ArrayList(requestMappingInfo.methodsCondition.methods)[0]
                ?: throw IllegalStateException("Request method mustn't be null!")
            val httpApiResource = HttpApiResource()
            httpApiResource.method = requestMethod
            val patterns = requestMappingInfo.patternsCondition?.patterns
            if (patterns?.isNotEmpty() == true) {
                httpApiResource.urlPattern = LinkedList(patterns)[0]
            } else {
                httpApiResource.urlPattern = "/url-pattern-not-found"
            }
            response.list.add(httpApiResource)
        }
        return ResponseBodyBean.ofSuccess(response)
    }
}
