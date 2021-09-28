package com.gndbiz.modules.core.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findUserByUsername(String username);

	boolean existsUserByUsername(String username);
}
