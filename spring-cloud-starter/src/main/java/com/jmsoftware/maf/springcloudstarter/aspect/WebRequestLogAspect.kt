package com.jmsoftware.maf.springcloudstarter.aspect

import cn.hutool.core.text.CharSequenceUtil
import cn.hutool.json.JSONUtil
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.util.RequestUtil
import com.jmsoftware.maf.springcloudstarter.util.UserUtil
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.Duration
import java.time.Instant

/**
 * # RequestLogAspect
 *
 * **Description**:
 *
 * RequestLogAspect is an AOP for logging URL, HTTP method, client IP and other information when web resource was
 * accessed.
 *
 * **Feature**:
 *
 * No methods in controller need to be decorated with annotation. This aspect would automatically cut the method
 * decorated with `@GetMapping` or `@PostMapping`.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 9:14 AM
 */
@Aspect
@Suppress("unused")
class WebRequestLogAspect(
    private val objectMapper: ObjectMapper
) {
    companion object {
        private val log = logger()
        private const val MAX_LENGTH_OF_JSON_STRING = 500
        private val BEFORE_TEMPLATE: String =
            """

            ============ WEB REQUEST LOG AOP (@Before) ============
            URL                : {}
            HTTP Method        : {}
            Client[IPv46:Port] : {}
            Username           : {}
            Class Method       : {}#{}
            Request Params     :
            {}
            """.trimIndent()
        private val AFTER_TEMPLATE: String =
            """

            ============ WEB REQUEST LOG AOP (@After) =============
            """.trimIndent()
        private val AROUND_TEMPLATE_FOR_JSON: String =
            """

            Response           :
            {}
            """.trimIndent()
        private val AROUND_TEMPLATE_FOR_NON_JSON: String =
            """

            Response (non-JSON): {}
            """.trimIndent()
        private val AROUND_TEMPLATE_END: String =
            """

            Elapsed time       : {} ({} ms)
            ============ WEB REQUEST LOG AOP (@Around) ============
            """.trimIndent()
        private val AFTER_THROWING_TEMPLATE: String =
            """

            Signature          : {}
            Exception          : {}, message: {}
            ======== WEB REQUEST LOG AOP (@AfterThrowing) =========
            """.trimIndent()
    }

    /**
     * Define pointcut. Pointcut is a predicate or expression that matches join points. In WebRequestLogAspect, we need
     * to cut any method annotated with `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`,
     * `@PatchMapping`, `@RequestMapping`.
     *
     * More detail at: [Spring AOP AspectJ pointcut expression examples](https://howtodoinjava.com/spring-aop/aspectj-pointcut-expressions/)
     */
    @Pointcut(
        "@annotation(org.springframework.web.bind.annotation.GetMapping)" +
                " || @annotation(org.springframework.web.bind.annotation.PostMapping)" +
                " || @annotation(org.springframework.web.bind.annotation.PutMapping)" +
                " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
                " || @annotation(org.springframework.web.bind.annotation.PatchMapping)" +
                " || @annotation(org.springframework.web.bind.annotation.RequestMapping)"
    )
    fun requestLogPointcut() {
        // Do nothing
    }

    /**
     * Before controller handle client request (on client sent a request).
     *
     * `@Before` annotated methods run exactly before the all methods matching with pointcut expression.
     *
     * @param joinPoint a point of execution of the program
     */
    @Before("requestLogPointcut()")
    fun beforeHandleRequest(joinPoint: JoinPoint) {
        val attributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val request = attributes.request
        log.info(
            BEFORE_TEMPLATE, request.requestURL.toString(), request.method,
            RequestUtil.getRequestIpAndPort(request), UserUtil.getCurrentUsername(),
            joinPoint.signature.declaringTypeName, joinPoint.signature.name,
            JSONUtil.toJsonStr(joinPoint.args)
        )
    }

    /**
     * `@After` annotated methods run exactly after the all methods matching with pointcut expression.
     */
    @After("requestLogPointcut()")
    fun afterHandleRequest() {
        log.info(AFTER_TEMPLATE)
    }

    /**
     * Around controller's method processes client request. Around advice can perform custom behavior before and after
     * the method invocation.
     *
     * @param proceedingJoinPoint the object can perform method invocation
     * @return any value (maybe void) that controller's method returned
     * @throws Throwable any exceptions that controller's method may throw
     */
    @Around("requestLogPointcut()")
    fun aroundHandleRequest(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val startInstant = Instant.now()
        val result = proceedingJoinPoint.proceed()
        val duration = Duration.between(startInstant, Instant.now())
        try {
            var formattedJsonString = JSONUtil.formatJsonStr(objectMapper.writeValueAsString(result))
            if (formattedJsonString.length > MAX_LENGTH_OF_JSON_STRING) {
                val substring = formattedJsonString.substring(0, MAX_LENGTH_OF_JSON_STRING - 1)
                formattedJsonString = CharSequenceUtil.format(
                    "{}… [The length({}) of JSON string is larger than the maximum({})]", substring,
                    formattedJsonString.length, MAX_LENGTH_OF_JSON_STRING
                )
            }
            log.info(AROUND_TEMPLATE_FOR_JSON, formattedJsonString)
        } catch (e: JsonProcessingException) {
            log.info(AROUND_TEMPLATE_FOR_NON_JSON, result)
        }
        log.info(AROUND_TEMPLATE_END, duration, duration.toMillis())
        return result
    }

    /**
     * `@AfterThrowing` annotated methods run after the method (matching with pointcut expression) exits by throwing an
     * exception.
     *
     * @param joinPoint a point of execution of the program
     * @param e         exception that controller's method throws
     */
    @AfterThrowing(pointcut = "requestLogPointcut()", throwing = "e")
    fun afterThrowingException(joinPoint: JoinPoint, e: Exception) {
        log.error(
            AFTER_THROWING_TEMPLATE,
            joinPoint.signature.toShortString(),
            e.toString(),
            e.message
        )
    }
}
