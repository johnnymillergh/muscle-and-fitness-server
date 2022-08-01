package com.jmsoftware.maf.common.domain.springbootstarter

import org.springframework.web.bind.annotation.RequestMethod

/**
 * # HttpApiResourcesResponse
 *
 * Description: HttpApiResourcesResponse, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/13/22 1:48 PM
 */
class HttpApiResourcesResponse {
    val list: MutableList<HttpApiResource> = mutableListOf()

    class HttpApiResource {
        var method: RequestMethod? = null
        var urlPattern: String? = null
    }
}
