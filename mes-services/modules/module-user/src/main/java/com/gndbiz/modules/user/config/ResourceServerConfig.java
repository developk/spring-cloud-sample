package com.gndbiz.modules.user.config;

import com.gndbiz.modules.core.support.handler.GndWebResponseTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private final GndWebResponseTranslator translator;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.headers().frameOptions().disable()
				.and()
				.antMatcher("/users/**")
				.authorizeRequests().anyRequest().authenticated()
				;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//		super.configure(resources);
		OAuth2AccessDeniedHandler deniedHandler = new OAuth2AccessDeniedHandler();
		deniedHandler.setExceptionTranslator(translator);
		OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
		entryPoint.setExceptionTranslator(translator);
		resources
				.stateless(true)
				.accessDeniedHandler(deniedHandler)
				.authenticationEntryPoint(entryPoint)
		;

	}

}
