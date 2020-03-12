package com.jmsoftware.authcenter.universal.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <h1>ErrorController</h1>
 * <p>
 * Error controller.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-02 16:56
 **/
@Slf4j
@RestController
@Api(tags = {"Error Controller"})
public class ErrorController extends BasicErrorController {
    public ErrorController(ErrorAttributes errorAttributes,
                           ServerProperties serverProperties,
                           List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus httpStatus = getStatus(request);
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        body.put("message", httpStatus.getReasonPhrase());
        log.error("Captured HTTP request error. Response body = {}", body);
        return new ResponseEntity<>(body, httpStatus);
    }
}
