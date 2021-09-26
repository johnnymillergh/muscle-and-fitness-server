package com.jmsoftware.maf.springcloudstarter.quartz.annotation;

import java.lang.annotation.*;

/**
 * Description: QuartzSchedulable, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 1:48 PM
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzSchedulable {
}
