package com.gndbiz.modules.core.domain.user;

import com.gndbiz.modules.core.support.service.BaseCrudService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService extends BaseCrudService<User> {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super(userRepository);
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	public boolean existsUserByUsername(String username) {
		return userRepository.existsUserByUsername(username);
	}

	@Override
	public User register(User user) {
		return super.register(getPasswordEncodedUser(user));
	}

	protected User getPasswordEncodedUser(User user) {
		return User.builder()
				.username(user.getUsername())
				.password(passwordEncoder.encode(user.getPassword()))
				.name(user.getName())
				.role(user.getRole())
				.picture(user.getPicture())
				.isAccountNonExpired(user.isAccountNonExpired())
				.isAccountNonLocked(user.isAccountNonLocked())
				.isCredentialsNonExpired(user.isCredentialsNonExpired())
				.isEnabled(user.isEnabled())
				.build();
	}
}
