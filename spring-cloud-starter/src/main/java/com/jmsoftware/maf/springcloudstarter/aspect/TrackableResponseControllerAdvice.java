package com.jmsoftware.maf.springcloudstarter.aspect;

import com.jmsoftware.maf.common.bean.TrackableBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

import static cn.hutool.core.text.StrFormatter.format;

/**
 * <h1>TrackableResponseControllerAdvice</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 3/12/22 1:55 AM
 **/
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class TrackableResponseControllerAdvice implements ResponseBodyAdvice<TrackableBean> {
    private final Tracer tracer;

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return TrackableBean.class.equals(returnType.getParameterType().getSuperclass());
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public TrackableBean beforeBodyWrite(
            TrackableBean body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        if (tracer.currentSpan() == null) {
            body.setTrack("No trace");
            return body;
        }
        val traceContext = Objects.requireNonNull(tracer.currentSpan()).context();
        body.setTrack(format("{}#{}", traceContext.traceId(), traceContext.spanId()));
        return body;
    }
}
