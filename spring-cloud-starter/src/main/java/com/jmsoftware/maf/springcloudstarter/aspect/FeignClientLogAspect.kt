package com.jmsoftware.maf.springcloudstarter.aspect

import cn.hutool.core.stream.StreamUtil
import cn.hutool.core.text.CharSequenceUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.json.JSONUtil
import com.jmsoftware.maf.common.util.logger
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*
import java.lang.reflect.Method
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * # FeignClientLogAspect
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 8:05 AM
 * @see <a href='https://github.com/spring-cloud/spring-cloud-openfeign/issues/322'>@FeignClient cannot be used as a pointcut by Spring aop</a>
 */
@Aspect
@Suppress("unused")
class FeignClientLogAspect {
    companion object {
        private val log = logger()
        private val REQUEST_MAPPING_SET = setOf(
            RequestMapping::class.java,
            GetMapping::class.java,
            PostMapping::class.java,
            PutMapping::class.java,
            DeleteMapping::class.java,
            PatchMapping::class.java
        )
        private val BEFORE_TEMPLATE: String =
            """

            ============ FEIGN CLIENT LOG (@Before) ============
            Feign URL            : [{}] {}
            Class Method         : {}#{}
            Request Params       :
            {}
            """.trimIndent()
        private val AROUND_TEMPLATE: String =
            """

            ============ FEIGN CLIENT LOG (@Around) ============
            Feign URL            : [{}] {}
            Class Method         : {}#{}
            Elapsed Time         : {} ms
            Feign Request Params :
            {}
            Feign Response Params:
            {}
            """.trimIndent()
    }

    @Pointcut(
        "@within(org.springframework.cloud.openfeign.FeignClient)" +
                " || @annotation(org.springframework.cloud.openfeign.FeignClient)"
    )
    fun feignClientPointcut() {
        // Do nothing
    }

    // @Before("feignClientPointcut()")
    fun beforeHandleRequest(joinPoint: JoinPoint) {
        val signature = joinPoint.signature as? MethodSignature ?: return
        log.info(
            BEFORE_TEMPLATE,
            getFeignMethod(signature).uppercase(Locale.getDefault()),
            getFeignUrl(signature),
            signature.declaringTypeName, signature.name,
            JSONUtil.toJsonStr(joinPoint.args)
        )
    }

    @Around("feignClientPointcut()")
    fun feignClientAround(joinPoint: ProceedingJoinPoint): Any {
        val startInstant = Instant.now()
        val result = joinPoint.proceed()
        val duration = Duration.between(startInstant, Instant.now())
        val signature = joinPoint.signature as? MethodSignature ?: return result
        val response: String = try {
            JSONUtil.toJsonStr(result)
        } catch (e: Exception) {
            log.warn("Failed to convert response to JSON string. {}", e.message)
            ObjectUtil.toString(result)
        }
        log.info(
            AROUND_TEMPLATE,
            getFeignMethod(signature).uppercase(Locale.getDefault()),
            getFeignUrl(signature),
            signature.declaringTypeName, signature.name,
            duration.toMillis(),
            JSONUtil.toJsonStr(joinPoint.args),
            response
        )
        return result
    }

    @Suppress("HttpUrlsUsage")
    private fun getFeignUrl(methodSignature: MethodSignature): String {
        val method = methodSignature.method
        val feignClientClass = method.declaringClass
        val feignClient = feignClientClass.getAnnotation(FeignClient::class.java)
        val feignUrl = StringBuilder("http://")
        concatDomain(feignClient, feignUrl)
        concatPath(method, feignUrl)
        return feignUrl.toString()
    }

    private fun getFeignMethod(methodSignature: MethodSignature): String {
        return Optional.ofNullable(methodSignature.method.annotations)
            .map { annotations: Array<Annotation> -> annotations[0] }
            .map { annotation: Annotation ->
                CharSequenceUtil.removeAll(annotation.annotationClass.simpleName, "Mapping")
            }
            .orElse("")
    }

    private fun concatPath(method: Method, feignUrl: StringBuilder) {
        StreamUtil.of(*method.annotations)
            .filter { annotation: Annotation -> REQUEST_MAPPING_SET.contains(annotation.annotationClass.java) }
            .findAny()
            .ifPresent { annotation: Annotation -> feignUrl.append(getFirstValue(annotation)) }
    }

    private fun getFirstValue(annotation: Annotation): String {
        if (annotation is RequestMapping) {
            return annotation.value[0]
        }
        if (annotation is GetMapping) {
            return annotation.value[0]
        }
        if (annotation is PostMapping) {
            return annotation.value[0]
        }
        if (annotation is PutMapping) {
            return annotation.value[0]
        }
        if (annotation is DeleteMapping) {
            return annotation.value[0]
        }
        return if (annotation is PatchMapping) {
            annotation.value[0]
        } else ""
    }

    private fun concatDomain(feignClient: FeignClient, feignUrl: StringBuilder) {
        if (CharSequenceUtil.isNotBlank(feignClient.value)) {
            feignUrl.append(feignClient.value)
            return
        }
        if (CharSequenceUtil.isNotBlank(feignClient.url)) {
            feignUrl.append(feignClient.url)
            return
        }
        if (CharSequenceUtil.isNotBlank(feignClient.name)) {
            feignUrl.append(feignClient.name)
            return
        }
        feignUrl.append("unrecognized")
    }
}
