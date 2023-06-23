package com.jmsoftware.maf.mafmis.remote

import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.mafmis.remote.OssCenterFeignClient.OssCenterFeignClientFallback
import jakarta.validation.constraints.NotNull
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

/**
 * # OssCenterFeignClient
 *
 * Description: OssCenterFeignClient, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 9:37 PM
 * @see <a href='https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/'>Spring Cloud OpenFeign</a>
 */
@Primary
@Validated
@FeignClient(value = OssCenterFeignClient.SERVICE_NAME, fallback = OssCenterFeignClientFallback::class)
interface OssCenterFeignClient {
    companion object {
        const val SERVICE_NAME = "oss-center"
    }

    /**
     * Upload single resource response body bean.
     *
     * @param multipartFile the multipart file
     * @return the response body bean
     */
    @PostMapping(value = ["/upload/single"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadSingleResource(@RequestPart("file") multipartFile: @NotNull MultipartFile): ResponseBodyBean<ObjectResponse>

    @Component
    class OssCenterFeignClientFallback : OssCenterFeignClient {
        companion object {
            private val log = logger()
        }

        override fun uploadSingleResource(multipartFile: MultipartFile): ResponseBodyBean<ObjectResponse> {
            log.error("Fallback -> OssCenterFeignClient#uploadSingleResource()")
            return ResponseBodyBean.ofFailureMessage("Fell back uploading single resource")
        }
    }
}
