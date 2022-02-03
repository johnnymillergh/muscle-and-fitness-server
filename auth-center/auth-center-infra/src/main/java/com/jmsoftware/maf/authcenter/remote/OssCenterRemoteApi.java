package com.jmsoftware.maf.authcenter.remote;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * Description: OssCenterRemoteApi, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/15/2021 11:10 AM
 * @see <a href='https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/'>Spring Cloud OpenFeign</a>
 **/
@Validated
@FeignClient(value = OssCenterRemoteApi.SERVICE_NAME, fallback = OssCenterRemoteApi.OssCenterRemoteApiFallback.class)
public interface OssCenterRemoteApi {
    String SERVICE_NAME = "oss-center";

    /**
     * Upload single resource response body bean.
     *
     * @param multipartFile the multipart file
     * @return the response body bean
     */
    @PostMapping(value = "/upload/single", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseBodyBean<ObjectResponse> uploadSingleResource(@NotNull @RequestPart("file") MultipartFile multipartFile);

    @Slf4j
    @Component
    class OssCenterRemoteApiFallback implements OssCenterRemoteApi {
        @Override
        public ResponseBodyBean<ObjectResponse> uploadSingleResource(@NotNull MultipartFile multipartFile) {
            log.error("Fallback -> OssCenterRemoteApi#uploadSingleResource()");
            return null;
        }
    }
}
