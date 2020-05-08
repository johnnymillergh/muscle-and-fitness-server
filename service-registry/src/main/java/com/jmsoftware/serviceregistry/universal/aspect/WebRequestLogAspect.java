package com.jmsoftware.serviceregistry.universal.aspect;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.common.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <h1>RequestLogAspect</h1>
 * <p><strong>Description</strong>:</p>
 * <p>RequestLogAspect is an AOP for logging URL, HTTP method, client IP and other information when web resource was
 * accessed.</p>
 * <p><strong>Feature</strong>:</p>
 * <p>No methods in controller need to be decorated with annotation. This aspect would automatically cut the method
 * decorated with `@GetMapping` or `@PostMapping`.</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-05-05 19:55
 **/
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class WebRequestLogAspect {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Define pointcut. Pointcut is a predicate or expression that matches join points. In WebRequestLogAspect, we need
     * to cut any method annotated with `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`, `@RequestMapping`.
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
        assert attributes != null;
        val request = attributes.getRequest();
        log.info("============ WEB REQUEST LOG START ============");
        log.info("URL                : {}", request.getRequestURL().toString());
        log.info("HTTP Method        : {}", request.getMethod());
        log.info("Client IP:Port     : {}", RequestUtil.getRequestIpAndPort(request));
        log.info("Class Method       : {}#{}",
                 joinPoint.getSignature().getDeclaringTypeName(),
                 joinPoint.getSignature().getName());
        log.info("Request Params     :{}{}", LINE_SEPARATOR, JSONUtil.toJsonPrettyStr(joinPoint.getArgs()));
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
        val startTime = System.currentTimeMillis();
        val result = proceedingJoinPoint.proceed();
        val elapsedTime = System.currentTimeMillis() - startTime;
        try {
            var formattedStringifiedJson = JSONUtil.formatJsonStr(mapper.writeValueAsString(result));
            if (formattedStringifiedJson.length() > 500) {
                formattedStringifiedJson = formattedStringifiedJson.substring(0, 499).concat("…");
            }
            log.info("Response           :{}{}", LINE_SEPARATOR, formattedStringifiedJson);
        } catch (JsonProcessingException e) {
            log.info("Response (non-JSON): {}", result);
        }
        log.info("Elapsed time       : {} s ({} ms)",
                 NumberUtil.decimalFormat("0.00", elapsedTime / 1000D),
                 elapsedTime);
        return result;
    }

    /**
     * `@After` annotated methods run exactly after the all methods matching with pointcut expression.
     */
    @After("requestLogPointcut()")
    public void afterHandleRequest() {
        log.info("============= WEB REQUEST LOG END =============");
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
        log.info("Signature          : {}", joinPoint.getSignature().toShortString());
        log.error("Exception message : {}, message: {}", e.toString(), e.getMessage());
        log.error("====== WEB REQUEST LOG END WITH EXCEPTION =====");
    }
}
