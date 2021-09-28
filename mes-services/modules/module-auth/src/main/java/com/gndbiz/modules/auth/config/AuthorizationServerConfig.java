package com.gndbiz.modules.auth.config;

import com.gndbiz.modules.auth.components.AuthProvider;
import com.gndbiz.modules.auth.components.AuthService;
import com.gndbiz.modules.core.support.handler.GndWebResponseTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


	private final ClientDetailsService clientDetailsService;

	private final AuthenticationManager authenticationManager;

	private final ResourceServerProperties resourceServerProperties;

	private final AuthService authService;

	private final AuthProvider authProvider;

	private final DataSource dataSource;

	private final GndWebResponseTranslator translator;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
				.withClientDetails(clientDetailsService)
		;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		super.configure(endpoints);

		endpoints
				.exceptionTranslator(translator)
				.authenticationManager(authenticationManager())
				.accessTokenConverter(jwtAccessTokenConverter())
				.tokenStore(tokenStore(dataSource))
				.reuseRefreshTokens(false)
				.userDetailsService(authService)
		;

	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		super.configure(security);

		security
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("permitAll()")
				;

	}

	@Bean
	@Primary
	public JdbcClientDetailsService jdbcClientDetailsService(DataSource dataSource) {
		return new JdbcClientDetailsService(dataSource);
	}

	@Bean
	public TokenStore tokenStore(DataSource dataSource) {
		return new JdbcTokenStore(dataSource);
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey(resourceServerProperties.getJwt().getKeyValue());
		return jwtAccessTokenConverter;
	}

	private AuthenticationManager authenticationManager() {
		return new ProviderManager(Collections.singletonList(authProvider));
	}

}
