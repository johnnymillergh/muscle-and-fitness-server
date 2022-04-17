package com.jmsoftware.maf.common.domain

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * <h1>ValidationTestPayload</h1>
 *
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/14/20 11:34 AM
 */
class ValidationTestPayload {
    @NotNull
    @Min(value = 1L)
    var id: Long = 0L

    @NotBlank
    lateinit var name: String
}
