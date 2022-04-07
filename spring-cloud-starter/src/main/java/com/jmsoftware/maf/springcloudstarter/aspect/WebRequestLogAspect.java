package com.jmsoftware.maf.springcloudstarter.aspect;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.springcloudstarter.util.RequestUtil;
import com.jmsoftware.maf.springcloudstarter.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.time.Instant;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.Objects.requireNonNull;

/**
 * <h1>RequestLogAspect</h1>
 * <p><strong>Description</strong>:</p>
 * <p>RequestLogAspect is an AOP for logging URL, HTTP method, client IP and other information when web resource was
 * accessed.</p>
 * <p><strong>Feature</strong>:</p>
 * <p>No methods in controller need to be decorated with annotation. This aspect would automatically cut the method
 * decorated with `@GetMapping` or `@PostMapping`.</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-05-05 19:55
 **/
@Slf4j
@Aspect
public class WebRequestLogAspect {
    private static final int MAX_LENGTH_OF_JSON_STRING = 500;
    private static final String BEFORE_TEMPLATE = """

            ============ WEB REQUEST LOG AOP (@Before) ============
            URL                : {}
            HTTP Method        : {}
            Client[IPv46:Port] : {}
            Username           : {}
            Class Method       : {}#{}
            Request Params     :
            {}
            """;
    private static final String AFTER_TEMPLATE = """

            ============ WEB REQUEST LOG AOP (@After) =============
            """;
    private static final String AROUND_TEMPLATE_FOR_JSON = """

            Response           :
            {}
            """;
    private static final String AROUND_TEMPLATE_FOR_NON_JSON ="""

            Response (non-JSON): {}
            """;
    private static final String AROUND_TEMPLATE_END = """

            Elapsed time       : {} ({} ms)
            ============ WEB REQUEST LOG AOP (@Around) ============
            """;
    private static final String AFTER_THROWING_TEMPLATE = """

            Signature          : {}
            Exception          : {}, message: {}
            ======== WEB REQUEST LOG AOP (@AfterThrowing) =========
            """;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Define pointcut. Pointcut is a predicate or expression that matches join points. In WebRequestLogAspect, we need
     * to cut any method annotated with `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`,
     * `@PatchMapping`, `@RequestMapping`.
     * <p>
     * More detail at: <a href="https://howtodoinjava.com/spring-aop/aspectj-pointcut-expressions/">Spring aop aspectJ
     * pointcut expression examples</a>
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PatchMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestLogPointcut() {
        // Do nothing
    }

    /**
     * Before controller handle client request (on client sent a request).
     * <p>
     * `@Before` annotated methods run exactly before the all methods matching with pointcut expression.
     *
     * @param joinPoint a point of execution of the program
     */
    @Before("requestLogPointcut()")
    public void beforeHandleRequest(JoinPoint joinPoint) {
        val attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        val request = requireNonNull(attributes).getRequest();
        log.info(BEFORE_TEMPLATE, request.getRequestURL().toString(), request.getMethod(),
                 RequestUtil.getRequestIpAndPort(request), UserUtil.getCurrentUsername(),
                 joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                 JSONUtil.toJsonStr(joinPoint.getArgs()));
    }

    /**
     * `@After` annotated methods run exactly after the all methods matching with pointcut expression.
     */
    @After("requestLogPointcut()")
    public void afterHandleRequest() {
        log.info(AFTER_TEMPLATE);
    }

    /**
     * Around controller's method processes client request. Around advice can perform custom behavior before and after
     * the method invocation.
     *
     * @param proceedingJoinPoint the object can perform method invocation
     * @return any value (may be void) that controller's method returned
     * @throws Throwable any exceptions that controller's method may throw
     */
    @Around("requestLogPointcut()")
    public Object aroundHandleRequest(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        val startInstant = Instant.now();
        val result = proceedingJoinPoint.proceed();
        val duration = Duration.between(startInstant, Instant.now());
        try {
            var formattedJsonString = JSONUtil.formatJsonStr(this.mapper.writeValueAsString(result));
            if (formattedJsonString.length() > MAX_LENGTH_OF_JSON_STRING) {
                val substring = formattedJsonString.substring(0, MAX_LENGTH_OF_JSON_STRING - 1);
                formattedJsonString =
                        format("{}… [The length({}) of JSON string is larger than the maximum({})]", substring,
                                      formattedJsonString.length(), MAX_LENGTH_OF_JSON_STRING);
            }
            log.info(AROUND_TEMPLATE_FOR_JSON, formattedJsonString);
        } catch (JsonProcessingException e) {
            log.info(AROUND_TEMPLATE_FOR_NON_JSON, result);
        }
        log.info(AROUND_TEMPLATE_END, duration, duration.toMillis());
        return result;
    }

    /**
     * `@AfterThrowing` annotated methods run after the method (matching with pointcut expression) exits by throwing an
     * exception.
     *
     * @param joinPoint a point of execution of the program
     * @param e         exception that controller's method throws
     */
    @AfterThrowing(pointcut = "requestLogPointcut()", throwing = "e")
    public void afterThrowingException(JoinPoint joinPoint, Exception e) {
        log.error(AFTER_THROWING_TEMPLATE, joinPoint.getSignature().toShortString(), e.toString(), e.getMessage());
    }
}
