package com.jmsoftware.maf.springcloudstarter.validation.annotation;

import java.lang.annotation.*;

/**
 * Description: DateTimeRangeGroup, change description here.
 * <p>
 * TODO: to support nullable value
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 6/3/2021 2:58 PM
 **/
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeRangeGroup {
    String groupName() default "defaultDateTimeRangeGroup";

    DateTimeRangeType type() default DateTimeRangeType.START_TIME;
}
