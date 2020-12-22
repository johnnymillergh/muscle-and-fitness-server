package com.jmsoftware.maf.muscleandfitnessserverreactivespringbootstarter.util;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

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
        exchange.getResponse().setStatusCode(httpStatus);
        val response = exchange.getResponse();
        JSON json = ResponseBodyBean.of(httpStatus.getReasonPhrase(), data, httpStatus.value());
        val responseBodyBytes = JSONUtil.toJsonStr(json).getBytes(StandardCharsets.UTF_8);
        DataBuffer dataBuffer = response.bufferFactory().wrap(responseBodyBytes);
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(dataBuffer));
    }
}
