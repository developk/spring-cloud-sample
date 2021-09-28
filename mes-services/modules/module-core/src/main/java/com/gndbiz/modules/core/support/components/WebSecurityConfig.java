package com.gndbiz.modules.core.support.components;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Order(SecurityProperties.BASIC_AUTH_ORDER)
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${spring.secret.user.name}")
	private String user;
	@Value("${spring.secret.user.password}")
	private String password;
	@Value("${spring.secret.user.roles}")
	private String roles;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.csrf().disable()
			.authorizeRequests()
//				.antMatchers("/users/**").permitAll()
				.antMatchers("/oauth/v3/api-docs", "/v3/api-docs", "/actuator/**").authenticated()
			.and()
            .formLogin().disable()
			.headers().disable()
            .httpBasic()
			.and()
            .sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser(user).password("{noop}" + password).roles(roles)
		;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
