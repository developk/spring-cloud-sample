package com.gndbiz.modules.user.form;

import com.gndbiz.modules.core.support.validation.annotations.UsernameUnique;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "UserForm", description = "사용자 정보 화면 입력 폼 데이터")
public class UserForm implements Serializable {

	@Schema(description = "사용자 PK")
	private int id;

	@NotBlank(message = "계정은 필수항목입니다.")
	@Size(min = 3, max = 255)
	@UsernameUnique
	@Schema(description = "사용자 계정")
	private String username;

	@NotBlank(message = "비밀번호는 필수항목입니다.")
	@Size(min = 3, max = 255)
	@Schema(description = "사용자 비밀번호")
	private String password;

	@NotBlank(message = "사용자 이름은 필수항목입니다.")
	@Size(min = 3, max = 100)
	@Schema(description = "사용자 이름")
	private String name;

	@NotBlank(message = "사용자 권한은 필수항목입니다.")
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

	@NotNull(message = "회사코드는 필수 항목입니다.")
	@Schema(description = "소속 회사 코드")
	private int companyId;
}
