package com.jmsoftware.maf.common.domain

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

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
