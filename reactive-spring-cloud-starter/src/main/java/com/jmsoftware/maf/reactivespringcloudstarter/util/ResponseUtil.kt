package com.jmsoftware.maf.reactivespringcloudstarter.util

import cn.hutool.core.text.CharSequenceUtil
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * Description: ResponseUtil, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:33 AM
 */
class ResponseUtil(
    private val objectMapper: ObjectMapper
) {
    /**
     * Render json mono.
     *
     * @param exchange   the exchange
     * @param httpStatus the http status
     * @param message    the message
     * @param data       the data
     * @return the mono
     */
    fun renderJson(
        exchange: ServerWebExchange,
        httpStatus: HttpStatus,
        message: String?,
        data: Any?
    ): Mono<Void> {
        exchange.response.statusCode = httpStatus
        val response = exchange.response
        response.statusCode = httpStatus
        // Set HTTP header, major browsers like Chrome now comply with the specification  and interpret correctly
        // UTF-8 special characters without requiring a charset=UTF-8 parameter.
        response.headers.contentType = MediaType.APPLICATION_JSON
        return response.writeWith(Mono.fromSupplier {
            val bufferFactory = response.bufferFactory()
            val message2 = CharSequenceUtil.format("{}. {}", httpStatus.reasonPhrase, message)
            val responseBody: Any = ResponseBodyBean.ofStatus(httpStatus.value(), message2, data)
            try {
                bufferFactory.wrap(objectMapper.writeValueAsBytes(responseBody))
            } catch (e: JsonProcessingException) {
                bufferFactory.wrap(ByteArray(0))
            }
        })
    }
}
