package com.gndbiz.modules.core.support.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {

	LOGIN("login"),
	LOGOUT("logout");

	private final String name;


}
