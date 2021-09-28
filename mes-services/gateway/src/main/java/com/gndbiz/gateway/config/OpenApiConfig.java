package com.gndbiz.gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Objects;

@Configuration
@OpenAPIDefinition(
		info = @Info(
				title = "STD v1.6 API 문서",
				version = "0.0.1-SNAPSHOT",
				description = "이 페이지는 STD v1.6 Microservice 들에 대한 API 문서 페이지 입니다.",
				contact = @Contact(name = "GNDBIZ", email = "admin@gndbiz.com", url = "http://www.gndbiz.com/kor/")
		)
)
@Profile("!prod")
public class OpenApiConfig {

	@Bean
	public CommandLineRunner apis(RouteDefinitionLocator locator, SwaggerUiConfigParameters swaggerUiParameters) {
//		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
//		if (null != definitions) {
//			definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
//				String name = String.valueOf(routeDefinition.getId()).replace("-service", "");
//				groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build());
//			});
//		}
		return args -> Objects.requireNonNull(locator.getRouteDefinitions().collectList().block())
				.stream()
				.map(RouteDefinition::getId)
				.filter(id -> id.matches(".*-service|auth-server"))
				.map(id -> id.replaceAll("-service|-server", ""))
				.forEach(swaggerUiParameters::addGroup);
	}

}
