package com.jmsoftware.maf.springcloudstarter.annotation;

import com.jmsoftware.maf.springcloudstarter.util.TreeElementType;

import java.lang.annotation.*;

/**
 * Description: TreeElement, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 5/28/2021 4:27 PM
 */
@Documented
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TreeElement {
    TreeElementType type() default TreeElementType.ID;
}
