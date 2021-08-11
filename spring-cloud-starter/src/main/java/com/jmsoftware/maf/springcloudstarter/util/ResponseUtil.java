package com.jmsoftware.maf.springcloudstarter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;

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
public class ResponseUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ResponseUtil() {
    }

    /**
     * Write data JSON to response.
     *
     * @param response   Response
     * @param httpStatus HTTP status
     * @param data       Data
     */
    public static void renderJson(final HttpServletResponse response, final HttpStatus httpStatus, final Object data) {
        standardizeHttpServletResponse(response, httpStatus);
        val responseBodyBean = ResponseBodyBean.ofStatus(httpStatus.value(),
                                                         httpStatus.getReasonPhrase(), data);
        try {
            response.getWriter().write(MAPPER.writeValueAsString(responseBodyBean));
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
    public static void renderJson(final HttpServletResponse response, final HttpStatus httpStatus,
                                  final String message) {
        standardizeHttpServletResponse(response, httpStatus);
        val responseBodyBean = ResponseBodyBean.ofStatus(httpStatus.value(), message, null);
        try {
            response.getWriter().write(MAPPER.writeValueAsString(responseBodyBean));
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
    public static void renderJson(final HttpServletResponse response, final BaseException exception) {
        val httpStatus = HttpStatus.valueOf(exception.getCode());
        standardizeHttpServletResponse(response, httpStatus);
        val responseBodyBean = ResponseBodyBean.ofStatus(exception.getCode(),
                                                         exception.getMessage(),
                                                         exception.getData());
        try {
            response.getWriter().write(MAPPER.writeValueAsString(responseBodyBean));
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
    private static void standardizeHttpServletResponse(final HttpServletResponse response,
                                                       final HttpStatus httpStatus) {
        response.setHeader("Access-Control-Allow-Origin", ALL);
        response.setHeader("Access-Control-Allow-Methods", ALL);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus.value());
    }
}
