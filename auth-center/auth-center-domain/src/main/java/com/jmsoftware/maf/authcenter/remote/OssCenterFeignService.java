/*
 * Copyright By ZATI
 * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
 * All rights reserved.
 *
 * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or
 * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your
 * system.
 */
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
