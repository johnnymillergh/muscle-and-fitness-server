package com.jmsoftware.maf.reactivespringbootstarter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description: ResponseUtil, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/21/2020 9:19 AM
 **/
public class ResponseUtil {
    /**
     * Render json mono.
     *
     * @param exchange   the exchange
     * @param httpStatus the http status
     * @param data       the data
     * @return the mono
     */
    @SneakyThrows
    public static Mono<Void> renderJson(@NonNull ServerWebExchange exchange, @NonNull HttpStatus httpStatus,
                                        @Nullable Object data) {
        ObjectMapper objectMapper = new ObjectMapper();
        exchange.getResponse().setStatusCode(httpStatus);
        val response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        // Set HTTP header, major browsers like Chrome now comply with the specification  and interpret correctly
        // UTF-8 special characters without requiring a charset=UTF-8 parameter.
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            final var responseBody = ResponseBodyBean.ofStatus(httpStatus, data);
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(responseBody));
            } catch (JsonProcessingException e) {
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
