package com.jmsoftware.maf.springcloudstarter.validation.annotation;

import com.jmsoftware.maf.springcloudstarter.validation.validator.DateTimeRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Description: DateTimeRangeConstraints, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 6/3/2021 2:08 PM
 **/
@Documented
@Constraint(validatedBy = DateTimeRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeRangeConstraints {
    String message() default "Invalid date time range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
