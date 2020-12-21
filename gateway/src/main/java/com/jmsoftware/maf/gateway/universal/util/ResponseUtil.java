package com.jmsoftware.maf.gateway.universal.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Date;

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
        val responseJson = new JSONObject();
        responseJson.getConfig().setIgnoreNullValue(false).setDateFormat("yyyy-MM-dd HH:mm:ss");
        responseJson.set("timestamp", new Date())
                .set("status", httpStatus.value())
                .set("message", httpStatus.getReasonPhrase())
                .set("data", data);
        val responseBodyBytes = JSONUtil.toJsonStr(responseJson).getBytes(StandardCharsets.UTF_8);
        DataBuffer dataBuffer = response.bufferFactory().wrap(responseBodyBytes);
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(dataBuffer));
    }
}
