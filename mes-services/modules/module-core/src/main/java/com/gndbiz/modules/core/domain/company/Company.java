package com.gndbiz.modules.core.domain.company;

import com.gndbiz.modules.core.domain.support.BaseEntity;
import com.gndbiz.modules.core.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@org.hibernate.annotations.Table(appliesTo = "tb_at_company", comment = "도입기업 관리 테이블")
@Entity(name = "company")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(columnDefinition = "VARCHAR(20) NOT NULL UNIQUE comment '회사코드'")
	private String code;

	@Column(columnDefinition = "VARCHAR(50) NOT NULL comment '회사명'")
	private String name;

	@Column(columnDefinition = "VARCHAR(255) comment '회사 로고'")
	private String logo;

	@Column(columnDefinition = "bit NOT NULL DEFAULT 0 comment '도입기업 만료 여부'")
	private boolean isCompanyNonExpired;

	@Column(columnDefinition = "bit NOT NULL DEFAULT 0 comment '도입기업 잠금 여부'")
	private boolean isCompanyNonLocked;

	@Column(columnDefinition = "bit NOT NULL DEFAULT 0 comment '도입기업 활성화 여부'")
	private boolean isEnabled;

	@OneToMany(mappedBy = "company")
	private List<User> userList;
}
