package com.jmsoftware.gateway.universal.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.LinkedList;
import java.util.Set;

/**
 * <h1>MethodArgumentValidationAspect</h1>
 * <p><span>This class is an aspect class that validates method&#39;s argument(s).</span></p>
 * <h2>USAGE (Must Do's)</h2>
 * <ol>
 * <li><span>Annotate the method which argument we need to validate by </span><code>@ValidateArgument</code></li>
 * <li><span>Annotate the argument(s) that we need to validate by </span><code>@javax.validation.Valid</code></li>
 * <li><span>The field(s) of the argument(s) could be annotated by the constraint annotation provided by Spring
 * Security</span></li>
 * </ol>
 * <h2>ATTENTION</h2>
 * <p><span>If the argument doesn&#39;t pass validation, an IllegalArgumentException will be thrown, and not proceed
 * the target method.</span></p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-07-06 12:17
 **/
@Slf4j
@Aspect
@Component
public class MethodArgumentValidationAspect {
    private final Validator validator;

    public MethodArgumentValidationAspect() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
        log.info("Validator for {} has been initiated.", this.getClass().getSimpleName());
    }

    /**
     * Define pointcut. Pointcut is a predicate or expression that matches join points. In
     * ValidateMethodArgumentAspect, we need to cut any method annotated with `@ValidateArgument` only.
     * <p>
     * More detail at: <a href="https://howtodoinjava.com/spring-aop/aspectj-pointcut-expressions/">Spring aop aspectJ
     * pointcut expression examples</a>
     */
    @Pointcut("@annotation(com.jmsoftware.gateway.universal.aspect.ValidateArgument)")
    public void validateMethodArgumentPointcut() {
    }

    /**
     * Before handle method's argument. This method will be executed after called `proceedingJoinPoint.proceed()`. In
     * this phrase, we're going to take some logs.
     * <p>
     * `@Before` annotated methods run exactly before the all methods matching with pointcut expression.
     *
     * @param joinPoint a point of execution of the program
     */
    @Before("validateMethodArgumentPointcut()")
    public void beforeMethodHandleArgument(JoinPoint joinPoint) {
        log.info("Method           : {}#{}",
                 joinPoint.getSignature().getDeclaringTypeName(),
                 joinPoint.getSignature().getName());
        log.info("Argument         : {}", joinPoint.getArgs());
    }

    /**
     * Around annotated method processes argument. Around advice can perform custom behavior before and after the
     * method invocation.
     *
     * @param proceedingJoinPoint the object can perform method invocation
     * @return any value (may be void) that annotated method returned
     */
    @Around("validateMethodArgumentPointcut()")
    public Object aroundMethodHandleArgument(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("======= METHOD'S ARGUMENT VALIDATION START =======");
        var args = proceedingJoinPoint.getArgs();
        var signature = (MethodSignature) proceedingJoinPoint.getSignature();
        var parameterAnnotations = signature.getMethod().getParameterAnnotations();
        // argumentIndexes is the array list that stores the index of argument we need to validate (the argument
        // annotated by `@Valid`)
        var argumentIndexListThatNeedsToBeValidated = new LinkedList<Integer>();
        for (var parameterAnnotation : parameterAnnotations) {
            int paramIndex = ArrayUtil.indexOf(parameterAnnotations, parameterAnnotation);
            for (var annotation : parameterAnnotation) {
                if (annotation instanceof Valid) {
                    argumentIndexListThatNeedsToBeValidated.add(paramIndex);
                }
            }
        }
        var errorMessageList = new LinkedList<String>();
        for (var index : argumentIndexListThatNeedsToBeValidated) {
            var constraintViolationSet = validator.validate(args[index]);
            if (CollUtil.isNotEmpty(constraintViolationSet)) {
                var errorMessage = String.format("Argument validation failed: %s",
                                                 getAllFieldErrorMessage(constraintViolationSet));
                errorMessageList.add(errorMessage);
            }
        }
        if (CollUtil.isNotEmpty(errorMessageList)) {
            var joinedErrorMessage = StrUtil.join(", ", errorMessageList);
            log.info("Method           : {}#{}", proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                     proceedingJoinPoint.getSignature().getName());
            log.info("Argument         : {}", args);
            log.error("Validation result: {}", joinedErrorMessage);
            // If the argument doesn't pass validation, an IllegalArgumentException will be thrown, and not
            // proceed the target method
            throw new IllegalArgumentException(joinedErrorMessage);
        }
        log.info("Validation result: Validation passed");
        return proceedingJoinPoint.proceed();
    }

    /**
     * `@After` annotated methods run exactly after the all methods matching with pointcut expression.
     */
    @After("validateMethodArgumentPointcut()")
    public void afterMethodHandleArgument() {
        log.info("======== METHOD'S ARGUMENT VALIDATION END ========");
    }

    /**
     * `@AfterThrowing` annotated methods run after the method (matching with pointcut expression) exits by throwing an
     * exception.
     *
     * @param joinPoint a point of execution of the program
     * @param e         exception that controller's method throws
     */
    @AfterThrowing(pointcut = "validateMethodArgumentPointcut()", throwing = "e")
    public void afterThrowingException(JoinPoint joinPoint, Exception e) {
        log.info("Signature        : {}", joinPoint.getSignature().toShortString());
        log.error("Exception message: {}", e.toString());
        log.error("== METHOD'S ARGUMENT VALIDATION END WITH EXCEPTION ==");
    }

    /**
     * Gets all field error message.
     *
     * @param constraintViolationSet the constraint violation set
     * @return the all field error message
     */
    private String getAllFieldErrorMessage(Set<ConstraintViolation<Object>> constraintViolationSet) {
        var allErrorMessageList = new LinkedList<String>();
        for (var constraintViolation : constraintViolationSet) {
            allErrorMessageList.add(String.format("invalid field: %s, %s", constraintViolation.getPropertyPath(),
                                                  constraintViolation.getMessage()));
        }
        return StrUtil.join("; ", allErrorMessageList);
    }
}
