package com.jmsoftware.maf.apigateway.universal.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.muscleandfitnessserverreactivespringbootstarter.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
        log.error("Exception occurred when [{}] requested access. Request URL: [{}] {}",
                  RequestUtil.getRequestIpAndPort(request), request.getMethod(), request.getURI());
        ServerHttpResponse response = exchange.getResponse();
        log.error(ex.getMessage(), ex.fillInStackTrace());
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        // Set HTTP header
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            final var responseBody = setResponseBody(response, ex);
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(responseBody));
            } catch (JsonProcessingException e) {
                log.warn("Exception occurred when writing response", e);
                return bufferFactory.wrap(new byte[0]);
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
        }
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
