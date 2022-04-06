package com.jmsoftware.maf.springcloudstarter.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * <h1>FeignClientLogAspect</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 2/1/22 1:10 PM
 * @see
 * <a href='https://github.com/spring-cloud/spring-cloud-openfeign/issues/322'>@FeignClient cannot be used as a pointcut by Spring aop</a>
 **/
@Slf4j
@Aspect
public class FeignClientLogAspect {
    private static final Set<Class<?>> REQUEST_MAPPING_SET = Collections.unmodifiableSet(CollUtil.newHashSet(
            RequestMapping.class,
            GetMapping.class,
            PostMapping.class,
            PutMapping.class,
            DeleteMapping.class,
            PatchMapping.class
    ));
    private static final String BEFORE_TEMPLATE = """

            ============ FEIGN CLIENT LOG (@Before) ============
            Feign URL            : [{}] {}
            Class Method         : {}#{}
            Request Params       :
            {}
            """;
    private static final String AROUND_TEMPLATE ="""

            ============ FEIGN CLIENT LOG (@Around) ============
            Feign URL            : [{}] {}
            Class Method         : {}#{}
            Elapsed Time         : {} ms
            Feign Request Params :
            {}
            Feign Response Params:
            {}
            """;

    @Pointcut("@within(org.springframework.cloud.openfeign.FeignClient)" +
            " || @annotation(org.springframework.cloud.openfeign.FeignClient)")
    public void feignClientPointcut() {
        // Do nothing
    }

    // @Before("feignClientPointcut()")
    @SuppressWarnings({"unused", "AlibabaCommentsMustBeJavadocFormat"})
    public void beforeHandleRequest(JoinPoint joinPoint) {
        val signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return;
        }
        log.info(BEFORE_TEMPLATE,
                 this.getFeignMethod((MethodSignature) signature).toUpperCase(),
                 this.getFeignUrl((MethodSignature) signature),
                 signature.getDeclaringTypeName(), signature.getName(),
                 JSONUtil.toJsonStr(joinPoint.getArgs()));
    }

    @Around("feignClientPointcut()")
    public Object feignClientAround(ProceedingJoinPoint joinPoint) throws Throwable {
        val startInstant = Instant.now();
        val result = joinPoint.proceed();
        val duration = Duration.between(startInstant, Instant.now());
        val signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return result;
        }
        String response;
        try {
            response = JSONUtil.toJsonStr(result);
        } catch (Exception e) {
            log.warn("Failed to convert response to JSON string. {}", e.getMessage());
            response = ObjectUtil.toString(result);
        }
        log.info(AROUND_TEMPLATE,
                 this.getFeignMethod((MethodSignature) signature).toUpperCase(),
                 this.getFeignUrl((MethodSignature) signature),
                 signature.getDeclaringTypeName(), signature.getName(),
                 duration.toMillis(),
                 JSONUtil.toJsonStr(joinPoint.getArgs()),
                 response);
        return result;
    }

    @SuppressWarnings("HttpUrlsUsage")
    private String getFeignUrl(MethodSignature methodSignature) {
        val method = methodSignature.getMethod();
        val feignClientClass = method.getDeclaringClass();
        val feignClient = feignClientClass.getAnnotation(FeignClient.class);
        val feignUrl = new StringBuilder("http://");
        this.concatDomain(feignClient, feignUrl);
        this.concatPath(method, feignUrl);
        return feignUrl.toString();
    }

    private String getFeignMethod(MethodSignature methodSignature) {
        return Optional.ofNullable(methodSignature.getMethod().getAnnotations())
                .map(annotations -> annotations[0])
                .map(annotation -> CharSequenceUtil.removeAll(
                        annotation.annotationType().getSimpleName(), "Mapping"
                ))
                .orElse("");
    }

    private void concatPath(Method method, StringBuilder feignUrl) {
        StreamUtil.of(method.getAnnotations())
                .filter(annotation -> REQUEST_MAPPING_SET.contains(annotation.annotationType()))
                .findAny()
                .ifPresent(annotation -> feignUrl.append(this.getFirstValue(annotation)));
    }

    private String getFirstValue(Annotation annotation) {
        if (annotation instanceof RequestMapping) {
            return ((RequestMapping) annotation).value()[0];
        }
        if (annotation instanceof GetMapping) {
            return ((GetMapping) annotation).value()[0];
        }
        if (annotation instanceof PostMapping) {
            return ((PostMapping) annotation).value()[0];
        }
        if (annotation instanceof PutMapping) {
            return ((PutMapping) annotation).value()[0];
        }
        if (annotation instanceof DeleteMapping) {
            return ((DeleteMapping) annotation).value()[0];
        }
        if (annotation instanceof PatchMapping) {
            return ((PatchMapping) annotation).value()[0];
        }
        return "";
    }

    private void concatDomain(FeignClient feignClient, StringBuilder feignUrl) {
        if (CharSequenceUtil.isNotBlank(feignClient.value())) {
            feignUrl.append(feignClient.value());
            return;
        }
        if (CharSequenceUtil.isNotBlank(feignClient.url())) {
            feignUrl.append(feignClient.url());
            return;
        }
        if (CharSequenceUtil.isNotBlank(feignClient.name())) {
            feignUrl.append(feignClient.name());
            return;
        }
        feignUrl.append("unrecognized");
    }
}
