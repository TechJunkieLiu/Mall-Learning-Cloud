package com.aiyangniu.gateway.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger资源配置
 * 在Spring Boot单体架构集成swagger时，是通过包路径进行业务分组，然后在前端进行不同模块的展示
 * 在Spring Cloud微服务架构下，单个服务类似于原来业务组，需要swagger-resource重写接口，由网关的注册中心动态发现所有的微服务文档
 * springfox-swagger提供的分组接口是swagger-resource，返回的是分组接口名称、地址等信息
 *
 * @author lzq
 * @date 2023/09/22
 */
@Slf4j
@Primary
@Configuration
@AllArgsConstructor
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;

    @Override
    public List<SwaggerResource> get() {
        // 接口资源列表
        List<SwaggerResource> resources = new ArrayList<>();
        // 服务名称列表
        List<String> routeHosts = new ArrayList<>();

        // 获取所有路由的ID(应用名称)
        routeLocator.getRoutes().subscribe(route -> routeHosts.add(route.getId()));

        // 过滤出配置文件中定义的路由 -> 过滤出Path Route Predicate -> 根据路径拼接成api-docs路径 -> 生成SwaggerResource
        gatewayProperties.getRoutes().stream()
                .filter(routeDefinition -> routeHosts.contains(routeDefinition.getId()))
                .forEach(route -> {
                    route.getPredicates().stream()
                            .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                            .forEach(predicateDefinition -> resources.add(
                                    swaggerResource(route.getId(), predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("**", "v2/api-docs"))
                                    )
                            );
                });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        log.info("name:{}, location:{}", name, location);
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
