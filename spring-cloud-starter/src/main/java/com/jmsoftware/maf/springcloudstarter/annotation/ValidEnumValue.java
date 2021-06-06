package com.jmsoftware.maf.springcloudstarter.annotation;

import com.jmsoftware.maf.springcloudstarter.validation.EnumValueValidator;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <h1>ValidEnumValue</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/29/21 12:31 PM
 **/
@Documented
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
@Constraint(validatedBy = EnumValueValidator.class)
public @interface ValidEnumValue {
    String message() default "Invalid enumeration value";

    Class<?> targetEnum() default Class.class;

    boolean ignoreNull() default false;

    Class[] groups() default {};

    Class[] payload() default {};
}
