package com.jmsoftware.maf.springcloudstarter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.exception.BaseException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.web.cors.CorsConfiguration.ALL;

/**
 * <h1>ResponseUtil</h1>
 * <p>
 * Response util
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 2/27/20 9:45 AM
 **/
@Slf4j
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ResponseUtil {
    private final ObjectMapper objectMapper;

    /**
     * Write data JSON to response.
     *
     * @param response   Response
     * @param httpStatus HTTP status
     * @param data       Data
     */
    public void renderJson(@NonNull HttpServletResponse response, @NonNull HttpStatus httpStatus,
                           @Nullable Object data) {
        this.standardizeHttpServletResponse(response, httpStatus);
        val responseBodyBean = ResponseBodyBean.ofStatus(httpStatus.value(),
                                                         httpStatus.getReasonPhrase(), data);
        try {
            response.getWriter().write(this.objectMapper.writeValueAsString(responseBodyBean));
        } catch (IOException e) {
            log.error("Error occurred when responding a data JSON.", e);
        }
    }

    /**
     * Render json.
     *
     * @param response   the response
     * @param httpStatus the http status
     * @param message    the message
     */
    public void renderJson(@NonNull HttpServletResponse response, @NonNull HttpStatus httpStatus,
                           @NonNull String message) {
        this.standardizeHttpServletResponse(response, httpStatus);
        val responseBodyBean = ResponseBodyBean.ofStatus(httpStatus.value(), message, null);
        try {
            response.getWriter().write(this.objectMapper.writeValueAsString(responseBodyBean));
        } catch (IOException e) {
            log.error("Error occurred when responding a data JSON.", e);
        }
    }

    /**
     * Write exception JSON to response.
     *
     * @param response  Response
     * @param exception Exception
     */
    public void renderJson(@NonNull HttpServletResponse response, @NonNull BaseException exception) {
        val httpStatus = HttpStatus.valueOf(exception.getCode());
        this.standardizeHttpServletResponse(response, httpStatus);
        val responseBodyBean = ResponseBodyBean.ofStatus(exception.getCode(),
                                                         exception.getMessage(),
                                                         exception.getData());
        try {
            response.getWriter().write(this.objectMapper.writeValueAsString(responseBodyBean));
        } catch (IOException e) {
            log.error("Error occurred when responding an exception JSON.", e);
        }
    }

    /**
     * Standardize http servlet response.
     *
     * @param response   the response
     * @param httpStatus the http status
     */
    private void standardizeHttpServletResponse(HttpServletResponse response, HttpStatus httpStatus) {
        response.setHeader("Access-Control-Allow-Origin", ALL);
        response.setHeader("Access-Control-Allow-Methods", ALL);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus.value());
    }
}
