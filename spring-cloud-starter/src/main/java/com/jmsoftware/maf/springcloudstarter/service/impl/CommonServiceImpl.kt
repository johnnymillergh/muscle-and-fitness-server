package com.jmsoftware.maf.springcloudstarter.service.impl

import cn.hutool.json.JSON
import cn.hutool.json.JSONUtil
import com.jmsoftware.maf.common.domain.ValidationTestPayload
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import com.jmsoftware.maf.springcloudstarter.service.CommonService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 * # CommonServiceImpl
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/13/22 2:01 PM
 */
@Service
class CommonServiceImpl(
    private val mafProjectProperties: MafProjectProperties
) : CommonService {
    companion object {
        private val log = logger()
    }

    @Value("\${greeting:Hello, World! (Embedded in Java)}")
    private val greeting: String? = null

    override fun getApplicationInfo(): JSON {
        return JSONUtil.parseObj(mafProjectProperties).set("greeting", greeting)
    }

    override fun validateObject(payload: @Valid @NotNull ValidationTestPayload) {
        log.info("Validation passed! {}", payload)
    }
}
