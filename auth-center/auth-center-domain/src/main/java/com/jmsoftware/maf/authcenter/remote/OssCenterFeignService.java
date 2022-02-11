package com.jmsoftware.maf.authcenter.remote;

import com.jmsoftware.maf.common.domain.osscenter.write.ObjectResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * Description: OssCenterFeignService, change description here.
 *
 * @author 钟俊 (za-zhongjun), email: jun.zhong@zatech.com, date: 2/5/2022 7:46 PM
 **/
@Validated
public interface OssCenterFeignService {
    /**
     * Upload single resource object response.
     *
     * @param multipartFile the multipart file
     * @return the object response
     */
    ObjectResponse uploadSingleResource(@NotNull MultipartFile multipartFile);
}
