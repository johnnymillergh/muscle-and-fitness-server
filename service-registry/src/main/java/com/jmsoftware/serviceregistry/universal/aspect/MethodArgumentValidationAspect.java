package com.jmsoftware.serviceregistry.universal.aspect;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.validation.*;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
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
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    /**
     * Define pointcut. Pointcut is a predicate or expression that matches join points. In
     * ValidateMethodArgumentAspect, we need to cut any method annotated with `@ValidateArgument` only.
     * <p>
     * More detail at: <a href="https://howtodoinjava.com/spring-aop/aspectj-pointcut-expressions/">Spring aop aspectJ
     * pointcut expression examples</a>
     */
    @Pointcut("@annotation(com.jmsoftware.serviceregistry.universal.aspect.ValidateArgument)")
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
        Object[] args = proceedingJoinPoint.getArgs();
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        // argumentIndexes is the array list that stores the index of argument we need to validate (the argument
        // annotated by `@Valid`)
        List<Integer> argumentIndexes = new ArrayList<>();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            int paramIndex = ArrayUtil.indexOf(parameterAnnotations, parameterAnnotation);
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof Valid) {
                    argumentIndexes.add(paramIndex);
                }
            }
        }

        for (Integer index : argumentIndexes) {
            Set<ConstraintViolation<Object>> validates = validator.validate(args[index]);
            if (CollectionUtil.isNotEmpty(validates)) {
                String message = String.format("Argument validation failed: %s", validates);
                log.info("Method           : {}#{}",
                         proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                         proceedingJoinPoint.getSignature().getName());
                log.info("Argument         : {}", args);
                log.error("Validation result: {}", message);
                // If the argument doesn't pass validation, an IllegalArgumentException will be thrown, and not
                // proceed the target method
                throw new IllegalArgumentException(message);
            }
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
}
