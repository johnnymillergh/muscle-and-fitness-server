package com.jmsoftware.maf.authcenter.permission.response

import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse

/**
 * Description: GetServicesInfoResponse, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/11/22 6:08 PM
 */
data class GetServicesInfoResponse(
    val list: List<ServiceInfo>
)

data class ServiceInfo(
    val serviceId: String,
    val httpApiResources: HttpApiResourcesResponse
)
