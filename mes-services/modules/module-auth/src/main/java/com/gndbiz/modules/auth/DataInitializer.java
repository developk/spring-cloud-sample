package com.gndbiz.modules.auth;

import com.gndbiz.modules.core.domain.user.User;
import com.gndbiz.modules.core.domain.user.UserRepository;
import com.gndbiz.modules.core.support.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 프로그램 시작 시 테스트용 계정 및 테스트용 Oauth 인증 클라이언트 정보를
 * DB에 입력해주는 Runner local 프로필에서만 활성화 됨.
 */
@Component
@Profile("local")
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final JdbcClientDetailsService jdbcClientDetailsService;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		// 실행 시 테스트 계정 생성
		User testUser = userRepository.findUserByUsername("user01");
		if (ObjectUtils.isEmpty(testUser)) {
			testUser = User.builder()
					           .username("user01")
					           .password(passwordEncoder.encode("user01"))
					           .name("테스트계정")
					           .role(Role.USER)
					           .build();

			userRepository.save(testUser);

		}

		try {
			jdbcClientDetailsService.loadClientByClientId("gndbiz");
		} catch(NoSuchClientException e) {
			ClientDetails details = new ClientDetails() {
					@Override
					public String getClientId() {
						return "gndbiz";
					}

					@Override
					public Set<String> getResourceIds() {
						return new HashSet<>();
					}

					@Override
					public boolean isSecretRequired() {
						return true;
					}

					@Override
					public String getClientSecret() {
						return passwordEncoder.encode("gndbiz1101");
					}

					@Override
					public boolean isScoped() {
						return true;
					}

					@Override
					public Set<String> getScope() {
						Set<String> scopes = new HashSet<>();
						scopes.add("read");
						scopes.add("write");
						return scopes;
					}

					@Override
					public Set<String> getAuthorizedGrantTypes() {
						Set<String> authorizedGrantTypes = new HashSet<>();
						authorizedGrantTypes.add("authorization_code");
						authorizedGrantTypes.add("password");
						authorizedGrantTypes.add("client_credentials");
						authorizedGrantTypes.add("implicit");
						authorizedGrantTypes.add("refresh_token");
						return authorizedGrantTypes;
					}

					@Override
					public Set<String> getRegisteredRedirectUri() {
						return new HashSet<>();
					}

					@Override
					public Collection<GrantedAuthority> getAuthorities() {
						List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
						authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
						return authorities;
					}

					@Override
					public Integer getAccessTokenValiditySeconds() {
						return 36000;
					}

					@Override
					public Integer getRefreshTokenValiditySeconds() {
						return 2592000;
					}

					@Override
					public boolean isAutoApprove(String scope) {
						return false;
					}

					@Override
					public Map<String, Object> getAdditionalInformation() {
						return new HashMap<>();
					}
				};
			jdbcClientDetailsService.addClientDetails(details);
		}


	}
}
