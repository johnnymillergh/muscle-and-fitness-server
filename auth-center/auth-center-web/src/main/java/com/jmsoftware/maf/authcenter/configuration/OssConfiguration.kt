package com.jmsoftware.maf.authcenter.configuration

import com.jmsoftware.maf.authcenter.remote.OssCenterFeignService
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.poi.OssUploader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mock.web.MockMultipartFile
import java.io.InputStream

/**
 * Description: OssConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/15/2021 11:51 AM
 */
@Configuration
class OssConfiguration(
    private val ossCenterFeignService: OssCenterFeignService
) {
    companion object {
        private val log = logger()
    }

    @Bean
    fun ossUploader(): OssUploader {
        return OssUploader { name: String, inputStream: InputStream ->
            val multipartFile = MockMultipartFile(name, name, null, inputStream)
            val objectResponse = ossCenterFeignService.uploadSingleResource(multipartFile)
            log.info("Uploaded multipartFile. objectResponse: $objectResponse")
            "${objectResponse.bucket}/${objectResponse.getObject()}"
        }
    }
}
