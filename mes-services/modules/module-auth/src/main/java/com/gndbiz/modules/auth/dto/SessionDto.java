package com.gndbiz.modules.auth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionDto implements Serializable {

	private int id;

	@NotBlank(message = "계정은 필수 입니다.")
	private String username;
	private String name;
	private String role;
	private String picture;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	@NotBlank(message = "oAuth 클라이언트 ID는 필수 입니다.")
	private String clientId;

}
