package com.gndbiz.modules.auth.components;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {


	private final PasswordEncoder passwordEncoder;

	private final AuthService authService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();

		UserDetails userDetails = authService.loadUserByUsername(username);

		if (ObjectUtils.isEmpty(userDetails)) {
			throw new AuthenticationCredentialsNotFoundException("계정 정보를 찾을 수 없습니다.");
		}

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("비밀번호가 잘못되었습니다.");
		}

		if (!userDetails.isEnabled()) {
			throw new DisabledException("사용중지 된 계정입니다.");
		}

		if (!userDetails.isAccountNonLocked()) {
			throw new LockedException("계정이 잠겼습니다.");
		}

		if (!userDetails.isCredentialsNonExpired()) {
			throw new AccountExpiredException("비밀번호가 만료되었습니다.");
		}

		if (!userDetails.isAccountNonExpired()) {
			throw new AccountExpiredException("계정이 만료되었습니다..");
		}

		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
		result.setDetails(userDetails);
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
