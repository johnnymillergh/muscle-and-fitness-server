package com.jmsoftware.exercisemis.universal.aspect;

import java.lang.annotation.*;

/**
 * <h1>ValidateArgument</h1>
 * <p>Annotation for validating method's argument.</p>
 * <h2>ATTENTION</h2>
 * <p><span>If the argument doesn&#39;t pass validation, an IllegalArgumentException will be thrown, and not proceed
 * the target method.</span></p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-07-06 12:08
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ValidateArgument {
}
