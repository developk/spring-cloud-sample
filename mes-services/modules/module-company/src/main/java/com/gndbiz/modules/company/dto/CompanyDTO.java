package com.gndbiz.modules.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CompanyDTO", description = "회사 정보 화면 출력 데이터")
public class CompanyDTO implements Serializable {
	@Schema(description = "회사 PK")
	private int id;
	@Schema(description = "회사 코드")
	private String code;
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
