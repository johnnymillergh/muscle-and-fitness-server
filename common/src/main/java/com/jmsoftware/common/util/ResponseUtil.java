package com.jmsoftware.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.common.bean.ResponseBodyBean;
import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <h1>ResponseUtil</h1>
 * <p>
 * Response util
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 2/27/20 9:45 AM
 **/
@Slf4j
@SuppressWarnings("unused")
public class ResponseUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Write data JSON to response.
     *
     * @param response   Response
     * @param httpStatus HTTP status
     * @param data       Data
     */
    public static void renderJson(final HttpServletResponse response, final HttpStatus httpStatus, final Object data) {
        try {
            ResponseBodyBean<Object> responseBodyBean = ResponseBodyBean.ofStatus(httpStatus.getCode(),
                                                                                  httpStatus.getMessage(),
                                                                                  data);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(httpStatus.getCode());
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
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(exception.getCode());
            ResponseBodyBean<Object> responseBodyBean = ResponseBodyBean.ofStatus(exception.getCode(),
                                                                                  exception.getMessage(),
                                                                                  exception.getData());
            response.getWriter().write(MAPPER.writeValueAsString(responseBodyBean));
        } catch (IOException e) {
            log.error("Error occurred when responding an exception JSON.", e);
        }
    }
}
