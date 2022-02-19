package com.jmsoftware.maf.authcenter.permission.response;

import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse;
import lombok.Data;

import java.util.List;

/**
 * Description: GetServicesInfoResponse, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/28/2020 9:30 AM
 **/
@Data
public class GetServicesInfoResponse {
    private List<ServiceInfo> list;

    @Data
    public static class ServiceInfo{
        private String serviceId;
        private HttpApiResourcesResponse httpApiResources;
    }
}
