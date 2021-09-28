package com.gndbiz.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	@Order(1)
	public SecurityWebFilterChain securityWebFilterChainHttpActuator(ServerHttpSecurity http) {
		http
				.securityMatcher(ServerWebExchangeMatchers.pathMatchers("/actuator/**", "/swagger-ui.html", "/v3/api-docs/**", "/webjars/**"))
				.authorizeExchange()
				.anyExchange().hasRole("ADMIN")
				.and()
				.csrf().disable()
				.formLogin().disable()
				.httpBasic()
		;

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http
				.securityMatcher(ServerWebExchangeMatchers.pathMatchers("/oauth/**", "/api/**"))
				.csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				.authorizeExchange()
				.anyExchange().permitAll()
		;

		return http.build();
	}

}
