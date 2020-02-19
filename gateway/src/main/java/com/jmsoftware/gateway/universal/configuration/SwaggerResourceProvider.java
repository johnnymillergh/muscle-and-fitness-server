package com.jmsoftware.gateway.universal.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>SwaggerResourceProvider</h1>
 * <p>
 * Change description here.
 * <p>
 * https://blog.csdn.net/ttzommed/article/details/81103609
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/15/20 5:40 PM
 **/
@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class SwaggerResourceProvider implements SwaggerResourcesProvider {
    public static final String API_URI = "/v2/api-docs";
    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;
    private final ProjectProperty projectProperty;

    /**
     * Generate Swagger resource.
     *
     * @return Swagger resource.
     * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
     * @date 2/15/20 11:06 PM
     */
    @Override
    public List<SwaggerResource> get() {
        var swaggerResourceList = new ArrayList<SwaggerResource>();
        routeLocator.getRoutes().subscribe(route -> {
            var serviceName = route.getUri().toString().substring(4).toLowerCase();
            log.info("Gateway found dynamic route for [{}] from subscription. {}", serviceName, route);
            var swaggerResource = new SwaggerResource();
            swaggerResource.setName(serviceName.substring(1).toUpperCase());
            swaggerResource.setLocation(String.format("%s%s", serviceName, API_URI));
            swaggerResource.setSwaggerVersion("2.0");
            swaggerResourceList.add(swaggerResource);
        });
        log.info("Pre defined GatewayProperties. {}", gatewayProperties);
        log.info("Swagger resource updated. {}", swaggerResourceList);
        return swaggerResourceList;
    }
}
