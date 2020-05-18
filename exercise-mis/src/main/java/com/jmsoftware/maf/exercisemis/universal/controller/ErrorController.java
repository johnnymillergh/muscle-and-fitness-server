package com.jmsoftware.maf.exercisemis.universal.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        val httpStatus = getStatus(request);
        val body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        body.put("message", httpStatus.getReasonPhrase());
        val optionalTrace = Optional.ofNullable(body.get("trace"));
        optionalTrace.ifPresent(trace -> {
            val message = body.get("message");
            val firstLineOfTrace = trace.toString().split("\\n")[0];
            val joinedMessage = String.format("%s %s", message, firstLineOfTrace);
            body.put("message", joinedMessage);
            body.put("trace", "Trace has been simplified. Refer to 'message'");
        });
        log.error("Captured HTTP request error. Response body = {}", body);
        return new ResponseEntity<>(body, httpStatus);
    }
}
