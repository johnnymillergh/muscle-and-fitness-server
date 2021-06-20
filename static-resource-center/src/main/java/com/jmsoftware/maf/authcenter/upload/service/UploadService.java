package com.jmsoftware.maf.authcenter.upload.service;

import com.jmsoftware.maf.common.exception.BusinessException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * <h1>UploadService</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/20/21 2:19 PM
 **/
@Validated
public interface UploadService {
    /**
     * Upload single resource string.
     *
     * @param multipartFile the multipart file
     * @return the string
     * @throws IOException       the io exception
     * @throws BusinessException the business exception
     */
    String uploadSingleResource(@NotNull MultipartFile multipartFile) throws IOException, BusinessException;
}
