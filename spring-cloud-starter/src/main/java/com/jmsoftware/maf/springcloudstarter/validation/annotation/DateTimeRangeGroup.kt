package com.jmsoftware.maf.springcloudstarter.validation.annotation


/**
 * # DateTimeRangeGroup
 *
 * Description: DateTimeRangeGroup, change description here.
 *
 * TODO: to support nullable value
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 9:53 AM
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DateTimeRangeGroup(
    val groupName: String = "defaultDateTimeRangeGroup",
    val type: DateTimeRangeType = DateTimeRangeType.START_TIME
)
