package com.jmsoftware.maf.apigateway.universal.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.exception.SecurityException;
import com.jmsoftware.maf.reactivespringbootstarter.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * <h1>GlobalExceptionHandler</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 12/23/20 7:45 AM
 **/
@Slf4j
@Order(-1)
@Component
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    @Override
    @SuppressWarnings("NullableProblems")
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        val request = exchange.getRequest();
        log.error("Exception occurred when [{}] requested access. Exception message: {}. Request URL: [{}] {}",
                  RequestUtil.getRequesterIpAndPort(request), ex.getMessage(), request.getMethod(), request.getURI(),
                  ex);
        val response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        // Set HTTP header, major browsers like Chrome now comply with the specification  and interpret correctly
        // UTF-8 special characters without requiring a charset=UTF-8 parameter.
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.fromSupplier(() -> {
            val bufferFactory = response.bufferFactory();
            val responseBody = setResponseBody(response, ex);
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(responseBody));
            } catch (JsonProcessingException e) {
                log.warn("Exception occurred when writing response", e);
                return bufferFactory.wrap(e.getMessage().getBytes(StandardCharsets.UTF_8));
            }
        }));
    }

    /**
     * Sets response body.
     *
     * @param response the response
     * @param ex       the ex
     * @return the response body
     */
    private ResponseBodyBean<?> setResponseBody(ServerHttpResponse response, Throwable ex) {
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
            return ResponseBodyBean.ofStatus(((ResponseStatusException) ex).getStatus());
        } else if (ex instanceof SecurityException) {
            HttpStatus status = HttpStatus.valueOf(((SecurityException) ex).getCode());
            response.setStatusCode(status);
            return ResponseBodyBean.ofStatus(status, ex.getMessage());
        } else if (ex instanceof WebClientResponseException) {
            val exception = (WebClientResponseException) ex;
            response.setStatusCode(exception.getStatusCode());
            try {
                return objectMapper.readValue(exception.getResponseBodyAsString(), ResponseBodyBean.class);
            } catch (JsonProcessingException e) {
                log.warn("Exception occurred when writing response", e);
                return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        }
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                         String.format("Exception message: %s", ex.getMessage()));
    }
}
