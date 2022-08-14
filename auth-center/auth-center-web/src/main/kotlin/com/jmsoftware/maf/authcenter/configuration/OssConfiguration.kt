package com.jmsoftware.maf.authcenter.configuration

import com.jmsoftware.maf.authcenter.remote.OssCenterFeignService
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.poi.OssUploader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mock.web.MockMultipartFile
import java.io.InputStream

/**
 * # OssConfiguration
 *
 * Description: OssConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 5:51 PM
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
            "${objectResponse.bucket}/${objectResponse.`object`}".apply {
                log.info("Uploaded multipartFile. Path: $this")
            }
        }
    }
}
