package com.gndbiz.modules.core.support.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	USER("user", "일반 사용자"),
	MANAGER("manager", "관리자");

	private static final String ROLE_PREFIX = "ROLE_";

	private final String name;
	private final String desc;

	public String getRoleType() {
		return ROLE_PREFIX.concat(name.toUpperCase());
	}

	public String getValue() {
		return name;
	}

	public boolean equals(String authority) {
		return this.getRoleType().equals(authority);
	}

}
