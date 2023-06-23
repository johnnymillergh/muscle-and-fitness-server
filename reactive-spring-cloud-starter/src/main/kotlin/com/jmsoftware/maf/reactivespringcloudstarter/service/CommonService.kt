package com.jmsoftware.maf.reactivespringcloudstarter.service

import cn.hutool.json.JSON
import com.jmsoftware.maf.common.domain.ValidationTestPayload
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

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
