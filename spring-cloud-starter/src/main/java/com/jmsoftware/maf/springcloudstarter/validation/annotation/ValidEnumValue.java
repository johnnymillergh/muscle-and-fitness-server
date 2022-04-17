package com.jmsoftware.maf.springcloudstarter.validation.annotation;

import com.jmsoftware.maf.springcloudstarter.validation.validator.EnumValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
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

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum> targetEnum() default Enum.class;

    boolean ignoreNull() default false;
}
