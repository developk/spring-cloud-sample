package com.gndbiz.modules.core.domain.loginHis;

import com.gndbiz.modules.core.domain.support.BaseEntity;
import com.gndbiz.modules.core.domain.user.User;
import com.gndbiz.modules.core.support.enums.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@org.hibernate.annotations.Table(appliesTo = "tb_at_login_history", comment = "로그인/로그아웃 이력 관리")
@Entity(name = "login_history")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistory extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(columnDefinition = "VARCHAR(255) NOT NULL comment '발급 token key값'")
	private String tokenId;

	@Column(columnDefinition = "VARCHAR(16) NOT NULL comment '접속 IP'")
	private String ipAddress;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "VARCHAR(10) NOT NULL comment '로그인/로그아웃 여부'")
	private LoginType loginType;

	@Column(columnDefinition = "DATETIME COMMENT '시각'")
	private LocalDateTime logAt;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", referencedColumnName = "id")
	private User user;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "companyId", referencedColumnName = "id")
//	private Company company;
}
