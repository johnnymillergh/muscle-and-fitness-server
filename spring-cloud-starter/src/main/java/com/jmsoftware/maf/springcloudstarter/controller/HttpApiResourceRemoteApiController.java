package com.jmsoftware.maf.springcloudstarter.controller;

import cn.hutool.core.collection.CollUtil;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse;
import com.jmsoftware.maf.springcloudstarter.helper.HttpApiScanHelper;
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

/**
 * Description: HttpApiResourceRemoteApiController, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/25/2020 4:33 PM
 **/
@RestController
@RequiredArgsConstructor
public class HttpApiResourceRemoteApiController {
    private final MafConfigurationProperties mafConfigurationProperties;
    private final HttpApiScanHelper httpApiScanHelper;

    @GetMapping("/http-api-resources")
    public ResponseBodyBean<HttpApiResourcesResponse> getHttpApiResource() {
        val handlerMethodMap = this.httpApiScanHelper.scan(this.mafConfigurationProperties.getIncludedPackageForHttpApiScan());
        HttpApiResourcesResponse response = new HttpApiResourcesResponse();
        handlerMethodMap.forEach((requestMappingInfo, handlerMethod) -> {
            val requestMethod = Optional.ofNullable(
                    new ArrayList<>(requestMappingInfo.getMethodsCondition().getMethods()).get(0))
                    .orElseThrow(() -> new RuntimeException("Request method mustn't be null!"));
            val httpApiResource = new HttpApiResourcesResponse.HttpApiResource();
            httpApiResource.setMethod(requestMethod);
            val patterns = Objects.requireNonNull(requestMappingInfo.getPatternsCondition()).getPatterns();
            if (CollUtil.isNotEmpty(patterns)) {
                httpApiResource.setUrlPattern(new LinkedList<>(patterns).get(0));
            } else {
                httpApiResource.setUrlPattern("/url-pattern-not-found");
            }
            response.getList().add(httpApiResource);
        });
        return ResponseBodyBean.ofSuccess(response);
    }
}
