package com.jmsoftware.maf.springcloudstarter.validation;

import cn.hutool.core.util.ObjectUtil;
import com.jmsoftware.maf.springcloudstarter.annotation.ValidEnumValue;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <h1>EnumValueValidator</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/29/21 12:34 PM
 **/
@Slf4j
public class EnumValueValidator implements ConstraintValidator<ValidEnumValue, Number> {
    private final static String METHOD_NAME = "getValue";
    private ValidEnumValue validEnumValue;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(ValidEnumValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        validEnumValue = constraintAnnotation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        val enumClass = validEnumValue.targetEnum();
        val ignoreNull = validEnumValue.ignoreNull();
        if (!enumClass.isEnum()) {
            log.warn("The given target enum class is not enum! {}", enumClass.getName());
            return false;
        }
        if (ignoreNull && ObjectUtil.isNull(value)) {
            return true;
        }
        val enumConstantArray = enumClass.getEnumConstants();
        Method method;
        try {
            method = enumClass.getMethod(METHOD_NAME);
        } catch (NoSuchMethodException | SecurityException e) {
            log.warn("Did not find the method {} in the class {}", METHOD_NAME, enumClass.getName());
            return false;
        }
        var validResult = false;
        try {
            for (var obj : enumConstantArray) {
                Object valueDeclaredInEnum = method.invoke(obj);
                if (!(valueDeclaredInEnum instanceof Number)) {
                    log.error("The value declared in enum is not an instance of Number!");
                    throw new IllegalArgumentException("The value declared in enum is not an instance of Number!");
                }
                if (Objects.deepEquals(value, valueDeclaredInEnum)) {
                    validResult = true;
                    break;
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.error("Exception occurred when invoking method! Exception message: {}", e.getMessage());
            return false;
        }
        return validResult;
    }
}
