package com.jmsoftware.maf.common.domain.springbootstarter;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Description: HttpApiResourcesResponse, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/25/2020 4:58 PM
 **/
@Data
public class HttpApiResourcesResponse {
    private List<HttpApiResource> list = Lists.newLinkedList();

    @Data
    public static class HttpApiResource {
        private RequestMethod method;
        private String urlPattern;
    }
}
