package com.gndbiz.modules.core.support.openapi;

import com.gndbiz.modules.core.support.enums.StatusType;
import com.gndbiz.modules.core.support.http.ResponseBean;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("!prod")
public class OpenApiConfig {
	/**
	 *
	 * @param title API 타이틀
	 * @param version API 버전
	 * @param description API 설명
	 * @param name 회사이름
	 * @param url 회사 홈페이지 주소
	 * @param email 회사 admin 이메일 주소
	 * @return
	 */
	@Bean
	@RefreshScope
	public OpenAPI openAPI(@Value("${springdoc.title}") String title,
	                       @Value("${spring.application.version}") String version,
	                       @Value("${springdoc.description}") String description,
	                       @Value("${springdoc.contact.name}") String name,
	                       @Value("${springdoc.contact.url}") String url,
	                       @Value("${springdoc.contact.email}") String email,
	                       @Value("${authTokenUrl}") String authTokenUrl
	) {

		Info info = new Info()
				.title(title)
				.version(version)
				.description(description)
				.contact(new Contact().name(name).url(url).email(email))
				;

		OAuthFlow oAuthFlow = new OAuthFlow();
		oAuthFlow.tokenUrl(authTokenUrl);

		return new OpenAPI()
				.components(
						new Components()
								.addSecuritySchemes("OAuth",
										new SecurityScheme()
												.type(SecurityScheme.Type.OAUTH2)
												.in(SecurityScheme.In.HEADER)
												.name("Authorization")
												.flows(
														new OAuthFlows().password(oAuthFlow)
												)
								)
				)
				.addSecurityItem(new SecurityRequirement().addList("OAuth"))
				.info(info)
				;
	}

	@Bean
	@RefreshScope
	public OpenApiCustomiser customiserDefaultsApiResponses() {
		return openApi -> {
			openApi.getPaths().values().forEach(
					pathItem -> pathItem.readOperations().forEach(operation -> {

						ApiResponses responses = operation.getResponses();

						// mapping된 api 요청으로 url 요청이 바인딩 된 이후에는 404는 발생할 일이 없으므로 제거
						responses.remove("404");

						responses.addApiResponse("400", getDefault400Response());
						responses.addApiResponse("401", getDefault401Response());
						responses.addApiResponse("405", getDefault405Response());

					})
			);
		};
	}

	protected ApiResponse getDefault400Response() {
		ApiResponse response = new ApiResponse()
				                          .description("잘못된 요청")
				;

		Content content = new Content()
					.addMediaType(
							org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
							new MediaType().schema(new Schema().$ref("#/components/schemas/ResponseBeanObject"))
					);
				;

		response.setContent(content);

		return response;
	}

	protected ApiResponse getDefault401Response() {
		ApiResponse response = new ApiResponse()
				.description("인증 실패")
				;

		Map<String, Example> examples = new HashMap<>();

		Example ex1 = new Example()
				.summary("token 제공되지 않음")
				.description("인증 헤더 token이 제공되지 않음")
				.value(new ResponseBean<>(StatusType.UNAUTHORIZED, "Full authentication is required to access this resource", null))
				;

		Example ex2 = new Example()
				.summary("유효하지 않은 token")
				.description("제공 된 token이 유효하지 않은 경우")
				.value(new ResponseBean<>(StatusType.INVALID_TOKEN, "Cannot convert access token to JSON", null))
				;

		Example ex3 = new Example()
				.summary("만료된 token")
				.description("제공 된 token이 만료된 경우")
				.value(new ResponseBean<>(StatusType.EXPIRED_TOKEN, "Access token expired: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.......", null))
				;

		examples.put("unauthorized", ex1);
		examples.put("invalid_token", ex2);
		examples.put("expired_token", ex3);

		Content content = new Content()
				.addMediaType(
						org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
						new MediaType()
								.schema(new Schema().$ref("#/components/schemas/ResponseBeanObject"))
								.examples(examples)
				);
		;

		response.setContent(content);

		return response;
	}

	protected ApiResponse getDefault405Response() {
		ApiResponse response = new ApiResponse()
				                          .description("허용되지 않은 Http Method 요청")
				;

		Content content = new Content()
				.addMediaType(
						org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
						new MediaType().schema(new Schema().$ref("#/components/schemas/ResponseBeanObject"))
				);
		;

		response.setContent(content);

		return response;
	}

}
