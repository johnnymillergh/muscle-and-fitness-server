package com.jmsoftware.maf.springcloudstarter.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cn.hutool.core.text.CharSequenceUtil.format;

/**
 * <h1>ErrorController</h1>
 * <p>
 * Error controller.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-02 16:56
 **/
@Slf4j
@RestController
public class GlobalErrorController extends BasicErrorController {
    public GlobalErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
                                 List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
    }

    @Override
    @SuppressWarnings("scwspring_Non-annotatedcontrollerpublicmethod")
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        val httpStatus = this.getStatus(request);
        val body = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        body.put("message", httpStatus.getReasonPhrase());
        val optionalTrace = Optional.ofNullable(body.get("trace"));
        optionalTrace.ifPresent(trace -> {
            val message = body.get("message");
            val firstLineOfTrace = trace.toString().split("\\n")[0];
            val joinedMessage = format("{} {}", message, firstLineOfTrace);
            body.put("message", joinedMessage);
            body.put("trace",
                     format("Trace has been simplified by {}. Refer to `message`", this.getClass().getName()));
        });
        log.error("Captured HTTP request error. Response body = {}", body);
        return new ResponseEntity<>(body, httpStatus);
    }
}
