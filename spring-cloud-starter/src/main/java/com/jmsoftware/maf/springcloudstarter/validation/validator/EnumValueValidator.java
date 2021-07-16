package com.jmsoftware.maf.springcloudstarter.validation.validator;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.jmsoftware.maf.common.bean.EnumerationBase;
import com.jmsoftware.maf.springcloudstarter.validation.annotation.ValidEnumValue;
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
    private String methodName;
    private ValidEnumValue validEnumValue;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(ValidEnumValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.validEnumValue = constraintAnnotation;
        final Method getValue = ReflectUtil.getMethodByName(EnumerationBase.class, "getValue");
        this.methodName = getValue.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        val enumClass = this.validEnumValue.targetEnum();
        val ignoreNull = this.validEnumValue.ignoreNull();
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
            method = enumClass.getMethod(this.methodName);
        } catch (NoSuchMethodException | SecurityException e) {
            log.warn("Did not find the method {} in the class {}", this.methodName, enumClass.getName());
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
