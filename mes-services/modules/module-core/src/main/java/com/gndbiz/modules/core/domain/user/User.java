package com.gndbiz.modules.core.domain.user;

import com.gndbiz.modules.core.domain.company.Company;
import com.gndbiz.modules.core.domain.loginHis.LoginHistory;
import com.gndbiz.modules.core.domain.support.BaseEntity;
import com.gndbiz.modules.core.support.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@org.hibernate.annotations.Table(appliesTo = "tb_at_user", comment = "회원 정보 마스터 데이터 관리 테이블")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "companyId"})})
@Entity(name = "user")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(columnDefinition = "VARCHAR(50) NOT NULL comment '사용자 계정'")
	private String username;

	@Column(columnDefinition = "VARCHAR(500) NOT NULL comment '사용자 비밀번호'")
	private String password;

	@Column(columnDefinition = "VARCHAR(100) comment '사용자 이름'")
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "VARCHAR(15) NOT NULL DEFAULT 'USER' comment '계정 권한 유형'")
	private Role role;

	@Column(columnDefinition = "VARCHAR(255) comment '사용자 사진 업로드 경로'")
	private String picture;

	@Column(columnDefinition = "bit NOT NULL DEFAULT 0 comment '계정 만료 여부'")
	private boolean isAccountNonExpired;

	@Column(columnDefinition = "bit NOT NULL DEFAULT 0 comment '계정 잠금 여부'")
	private boolean isAccountNonLocked;

	@Column(columnDefinition = "bit NOT NULL DEFAULT 0 comment '계정 패스워드 만료 여부'")
	private boolean isCredentialsNonExpired;

	@Column(columnDefinition = "bit NOT NULL DEFAULT 0 comment '계정 사용 여부'")
	private boolean isEnabled;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "companyId", referencedColumnName = "id")
	private Company company;

	@OneToMany(mappedBy = "user")
	private List<LoginHistory> loginHistories;
}
