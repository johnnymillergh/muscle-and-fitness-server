package com.jmsoftware.maf.springcloudstarter.service

import cn.hutool.json.JSON
import com.jmsoftware.maf.common.domain.ValidationTestPayload
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 * # CommonService
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:35 AM
 */
@Validated
interface CommonService {
    /**
     * Gets application info.
     *
     * @return the application info.
     */
    fun getApplicationInfo(): JSON

    /**
     * Validate object.
     *
     * @param payload the payload
     */
    fun validateObject(payload: @Valid @NotNull ValidationTestPayload)
}
