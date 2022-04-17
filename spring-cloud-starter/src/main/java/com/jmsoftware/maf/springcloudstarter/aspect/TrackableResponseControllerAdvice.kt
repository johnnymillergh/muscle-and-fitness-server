package com.jmsoftware.maf.springcloudstarter.aspect

import com.jmsoftware.maf.common.bean.TrackableBean
import org.springframework.cloud.sleuth.Tracer
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * # TrackableResponseControllerAdvice
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 9:48 PM
 */
@RestControllerAdvice
class TrackableResponseControllerAdvice(
    private val tracer: Tracer
) : ResponseBodyAdvice<TrackableBean> {
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        return TrackableBean::class.java == returnType.parameterType.superclass
    }

    override fun beforeBodyWrite(
        body: TrackableBean?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): TrackableBean? {
        if (tracer.currentSpan() == null) {
            body?.track = "No trace"
            return body
        }
        val traceContext = tracer.currentSpan()?.context()
        body?.track = "${traceContext?.traceId()}#${traceContext?.spanId()}"
        return body
    }
}
