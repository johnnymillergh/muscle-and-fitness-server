package com.jmsoftware.maf.apigateway.handler

import cn.hutool.core.text.CharSequenceUtil
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.exception.SecurityException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.reactivespringcloudstarter.util.RequestUtil
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

/**
 * # GlobalExceptionHandler
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 4/16/22 11:49 AM
 **/
@Order(-1)
@Component
class GlobalExceptionHandler(
    private val objectMapper: ObjectMapper
) : ErrorWebExceptionHandler {
    companion object {
        private val log = logger()
    }

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val request = exchange.request
        log.error(
            "Exception occurred when [{}] requested access. Exception message: {}. Request URL: [{}] {}",
            RequestUtil.getRequesterIpAndPort(request), ex.message, request.method, request.uri
        )
        val response = exchange.response
        if (response.isCommitted) {
            return Mono.error(ex)
        }
        // Set HTTP header, major browsers like Chrome now comply with the specification  and interpret correctly
        // UTF-8 special characters without requiring a charset=UTF-8 parameter.
        response.headers.contentType = MediaType.APPLICATION_JSON
        return response.writeWith(Mono.fromSupplier {
            val bufferFactory = response.bufferFactory()
            val responseBody: ResponseBodyBean<*> = setResponseBody(response, ex)
            try {
                return@fromSupplier bufferFactory.wrap(objectMapper.writeValueAsBytes(responseBody))
            } catch (e: JsonProcessingException) {
                log.warn("Exception occurred when writing response", e)
                return@fromSupplier bufferFactory.wrap(e.message!!.toByteArray(StandardCharsets.UTF_8))
            }
        })
    }

    private fun setResponseBody(response: ServerHttpResponse, ex: Throwable): ResponseBodyBean<*> {
        when (ex) {
            is ResponseStatusException -> {
                response.statusCode = ex.status
                return ResponseBodyBean.ofStatus<Any>(ex.status)
            }
            is SecurityException -> {
                val status = HttpStatus.valueOf(ex.code)
                response.statusCode = status
                return ResponseBodyBean.ofStatus<Any>(status, ex.message)
            }
            is WebClientResponseException -> {
                response.statusCode = ex.statusCode
                return try {
                    objectMapper.readValue(ex.responseBodyAsString, ResponseBodyBean::class.java)
                } catch (e: JsonProcessingException) {
                    log.warn("Exception occurred when writing response. Exception message: ${e.message}")
                    ResponseBodyBean.ofStatus<Any>(ex.statusCode, ex.getResponseBodyAsString(StandardCharsets.UTF_8))
                }
            }
            else -> {
                response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
                return ResponseBodyBean.ofStatus<Any>(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    CharSequenceUtil.format("Exception message: {}", ex.message)
                )
            }
        }
    }
}
