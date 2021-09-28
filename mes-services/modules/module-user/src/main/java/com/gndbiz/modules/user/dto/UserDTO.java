package com.gndbiz.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserDTO", description = "사용자 정보 화면 출력 데이터")
public class UserDTO implements Serializable {
	@Schema(description = "사용자 PK")
	private int id;
	@Schema(description = "사용자 계정")
	private String username;
	@Schema(description = "사용자 이름")
	private String name;
	@Schema(description = "사용자 권한 유형")
	private String role;
	@Schema(description = "사용자 프로필사진 업로드 경로")
	private String picture;
	@Schema(description = "계정 만료 여부")
	private boolean accountNonExpired;
	@Schema(description = "계정 잠김 여부")
	private boolean accountNonLocked;
	@Schema(description = "비밀번호 만료 여부")
	private boolean credentialsNonExpired;
	@Schema(description = "계정 활성화 여부")
	private boolean enabled;
	@Schema(description = "소속 회사 코드")
	private int companyId;
}
