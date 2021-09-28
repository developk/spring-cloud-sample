package com.gndbiz.modules.auth.components;

import com.gndbiz.modules.core.domain.user.User;
import com.gndbiz.modules.core.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findUserByUsername(username);

		if (!ObjectUtils.isEmpty(user)) {
			return createUserSession(user);
		} else {
			return null;
		}

	}

	private org.springframework.security.core.userdetails.User createUserSession(User user) {
		Collection<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority(user.getRole().getRoleType()));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.isEnabled(), user.isAccountNonExpired(),
				user.isCredentialsNonExpired(), user.isAccountNonExpired(),
				roles);
	}
}
