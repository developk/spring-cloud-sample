package com.gndbiz.modules.company.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "companyForm", description = "회사 정보 화면 입력 폼 데이터")
public class CompanyForm implements Serializable {
	@Schema(description = "회사 PK")
	private int id;
	@NotBlank
	@Size(min = 3, max = 20)
	@Schema(description = "회사 코드")
	private String code;
	@NotBlank
	@Size(min = 1, max = 50)
	@Schema(description = "회사명")
	private String name;
	@Schema(description = "회사 로고 이미지 업로드 경로")
	private String logo;
	@Schema(description = "회사 만료 여부")
	private boolean companyNonExpired;
	@Schema(description = "회사 잠금 여부")
	private boolean companyNonLocked;
	@Schema(description = "회사 활성화 여부")
	private boolean enabled;
}
