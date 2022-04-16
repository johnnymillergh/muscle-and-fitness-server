package com.jmsoftware.maf.springcloudstarter.validation.annotation

import com.jmsoftware.maf.springcloudstarter.validation.validator.DateTimeRangeValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * # DateTimeRangeConstraints
 *
 * Description: DateTimeRangeConstraints, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 9:52 AM
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DateTimeRangeValidator::class])
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class DateTimeRangeConstraints(
    val message: String = "Invalid date time range",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
