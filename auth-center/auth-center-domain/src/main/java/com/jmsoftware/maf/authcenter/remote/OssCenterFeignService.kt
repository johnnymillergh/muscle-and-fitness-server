package com.jmsoftware.maf.authcenter.remote

import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotNull

/**
 * # OssCenterFeignService
 *
 * Description: OssCenterFeignService, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/10/22 11:57 AM
 */
@Validated
interface OssCenterFeignService {
    /**
     * Upload single resource object response.
     *
     * @param multipartFile the multipart file
     * @return the object response
     */
    fun uploadSingleResource(multipartFile: @NotNull MultipartFile): ObjectResponse
}
