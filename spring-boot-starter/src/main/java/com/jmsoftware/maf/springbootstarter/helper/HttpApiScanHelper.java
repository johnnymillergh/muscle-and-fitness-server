package com.jmsoftware.maf.springbootstarter.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description: HttpApiScanHelper, scanning HTTP API for microservice.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/25/2020 2:43 PM
 **/
@Slf4j
@Validated
@RequiredArgsConstructor
public class HttpApiScanHelper {
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private static final String EXCLUDED_PACKAGE = "com.jmsoftware.maf.springbootstarter";

    /**
     * Scan map.
     *
     * @param includedPackage the included package
     * @return the map
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/25/2020 4:02 PM
     */
    public Map<RequestMappingInfo, HandlerMethod> scan(@NotBlank String includedPackage) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        log.debug("Scanned request mapping info: {}", handlerMethods);
        val filteredHandlerMethods = new LinkedHashMap<RequestMappingInfo, HandlerMethod>();
        handlerMethods.forEach((requestMappingInfo, handlerMethod) -> {
            if (handlerMethod.toString().contains(includedPackage) &&
                    !handlerMethod.toString().contains(EXCLUDED_PACKAGE)) {
                try {
                    RequestMethod requestMethod = new ArrayList<>(
                            requestMappingInfo.getMethodsCondition().getMethods()).get(0);
                    log.debug("Request: [{}] {}, handler method: {}", requestMethod,
                             requestMappingInfo.getPatternsCondition(), handlerMethod);
                    filteredHandlerMethods.put(requestMappingInfo, handlerMethod);
                } catch (Exception e) {
                    log.warn(
                            "Exception occurred when getting request method. Exception message: {}. Request mapping " +
                                    "info: {}",
                            e.getMessage(), requestMappingInfo);
                }
            }
        });
        return filteredHandlerMethods;
    }
}
