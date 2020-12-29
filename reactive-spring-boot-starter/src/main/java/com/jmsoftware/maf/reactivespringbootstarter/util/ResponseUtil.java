package com.jmsoftware.maf.reactivespringbootstarter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
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
     * @param message    the message
     * @param data       the data
     * @return the mono
     */
    @SneakyThrows
    public static Mono<Void> renderJson(@NonNull ServerWebExchange exchange, @NonNull HttpStatus httpStatus,
                                        @Nullable String message, @Nullable Object data) {
        val objectMapper = new ObjectMapper();
        exchange.getResponse().setStatusCode(httpStatus);
        val response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        // Set HTTP header, major browsers like Chrome now comply with the specification  and interpret correctly
        // UTF-8 special characters without requiring a charset=UTF-8 parameter.
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.fromSupplier(() -> {
            val bufferFactory = response.bufferFactory();
            val message2 = String.format("%s. %s", httpStatus.getReasonPhrase(), message);
            val responseBody = ResponseBodyBean.ofStatus(httpStatus.value(), message2, data);
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(responseBody));
            } catch (JsonProcessingException e) {
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
