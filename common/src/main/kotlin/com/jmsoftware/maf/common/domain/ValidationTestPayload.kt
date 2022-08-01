package com.jmsoftware.maf.common.domain

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * # ValidationTestPayload
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/18/22 9:18 PM
 */
class ValidationTestPayload {
    @NotNull
    @Min(value = 1L)
    var id: Long = 0L

    @NotBlank
    lateinit var name: String
}
