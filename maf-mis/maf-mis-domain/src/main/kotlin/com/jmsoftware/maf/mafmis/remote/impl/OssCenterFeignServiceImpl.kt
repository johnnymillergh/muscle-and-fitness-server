package com.jmsoftware.maf.mafmis.remote.impl

import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse
import com.jmsoftware.maf.common.exception.InternalServerException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.mafmis.remote.OssCenterFeignClient
import com.jmsoftware.maf.mafmis.remote.OssCenterFeignService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

/**
 * # OssCenterFeignServiceImpl
 *
 * Description: OssCenterFeignServiceImpl, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/5/2022 7:47 PM
 */
@Service
class OssCenterFeignServiceImpl(
    private val ossCenterFeignClient: OssCenterFeignClient
) : OssCenterFeignService {
    companion object {
        private val log = logger()
    }

    override fun uploadSingleResource(multipartFile: MultipartFile): ObjectResponse {
        log.info("Uploading single resource to oss center. multipartFile: $multipartFile")
        return Optional.ofNullable(ossCenterFeignClient.uploadSingleResource(multipartFile))
            .map { response -> response.data!! }
            .orElseThrow {
                log.error("Failed to upload single resource to oss center. multipartFile: ${multipartFile.name}")
                InternalServerException("Failed to upload single resource to oss center")
            }
    }
}
