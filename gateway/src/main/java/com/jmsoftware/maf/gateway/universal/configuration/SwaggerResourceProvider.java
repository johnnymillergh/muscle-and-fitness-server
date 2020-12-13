package com.jmsoftware.maf.gateway.universal.configuration;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.LinkedList;
import java.util.List;

/**
 * <h1>SwaggerResourceProvider</h1>
 * <p>
 * Change description here.
 * <p>
 * https://blog.csdn.net/ttzommed/article/details/81103609
 * <p>
 * TODO: Refactor this class.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/15/20 5:40 PM
 * @see
 * <a href='https://doc.xiaominfo.com/guide/ui-front-gateway.html#%E6%96%87%E6%A1%A3%E8%81%9A%E5%90%88%E4%B8%9A%E5%8A%A1%E7%BC%96%E7%A0%81'>文档聚合业务编码</a>
 **/
@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class SwaggerResourceProvider implements SwaggerResourcesProvider {
    public static final String SWAGGER_API_URI = "/v2/api-docs";
    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;
    private final ProjectProperty projectProperty;
    private final CustomConfiguration customConfiguration;

    /**
     * Generate Swagger resource.
     *
     * @return Swagger resource.
     * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
     * @date 2/15/20 11:06 PM
     */
    @Override
    public List<SwaggerResource> get() {
        val swaggerResourceList = new LinkedList<SwaggerResource>();
        routeLocator.getRoutes().subscribe(route -> {
            val serviceName = route.getUri().toString().substring(5).toLowerCase();
            log.info("{} found dynamic route for [{}] from subscription. {}", projectProperty.getProjectArtifactId(),
                     serviceName, route);
            val swaggerResource = new SwaggerResource();
            swaggerResource.setName(serviceName.toUpperCase());
            swaggerResource.setLocation(String.format("%s%s", serviceName, SWAGGER_API_URI));
            swaggerResource.setSwaggerVersion("2.0");
            log.info("Got allowed application list: {}", customConfiguration.getAllowedApplicationList());
            if (CollUtil.isEmpty(customConfiguration.getAllowedApplicationList())) {
                log.warn("Allowed application list is not configured. Swagger is able to access any applications.");
                swaggerResourceList.add(swaggerResource);
            } else {
                customConfiguration.getAllowedApplicationList().forEach(allocationName -> {
                    if (StrUtil.compareIgnoreCase(serviceName, allocationName, false) == 0) {
                        log.warn("Swagger is adding resource. {}", JSONUtil.toJsonStr(swaggerResource));
                        swaggerResourceList.add(swaggerResource);
                    }
                });
            }
        });
        log.info("Pre defined GatewayProperties. {}", gatewayProperties);
        return swaggerResourceList;
    }
}
