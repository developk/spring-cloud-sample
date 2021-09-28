package com.gndbiz.modules.auth.controller;


import com.gndbiz.commons.net.NetworkUtils;
import com.gndbiz.modules.auth.dto.SessionDto;
import com.gndbiz.modules.auth.mapper.SessionMapper;
import com.gndbiz.modules.core.domain.loginHis.LoginHistory;
import com.gndbiz.modules.core.domain.loginHis.LoginHistoryService;
import com.gndbiz.modules.core.domain.user.User;
import com.gndbiz.modules.core.domain.user.UserService;
import com.gndbiz.modules.core.support.enums.LoginType;
import com.gndbiz.modules.core.support.enums.StatusType;
import com.gndbiz.modules.core.support.http.RequestBean;
import com.gndbiz.modules.core.support.http.ResponseBean;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class AuthController {

	private final TokenStore tokenStore;

	private final LoginHistoryService loginHistoryService;

	private final UserService userService;

	private final SessionMapper sessionMapper;

	@PostMapping(value = "/login")
	@Operation(description = "사용자 정보를 가져옵니다.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "session dto", required = true))
	public ResponseBean<SessionDto> login(@RequestBody @Valid RequestBean<SessionDto> sessionDto, HttpServletRequest request) {

		User user = userService.findUserByUsername(sessionDto.getUsername());

		if (null == user) {
			return new ResponseBean<>(StatusType.NOT_FOUND, "계정을 찾을 수 없습니다.", null);
		}

		SessionDto session = sessionMapper.toDto(user);
		session.setClientId(sessionDto.getData().getClientId());

		return new ResponseBean<>(StatusType.OK, session);
	}

	@PostMapping("/logout")
	@Operation(description = "로그아웃 처리합니다.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "session dto", required = true))
	public ResponseBean<SessionDto> logout(@RequestBody @Valid RequestBean<SessionDto> sessionDto, HttpServletRequest request) {

		// 로그아웃 로그 기록
		User user = userService.findUserByUsername(sessionDto.getUsername());

		if (null == user) {
			return new ResponseBean<>(StatusType.NOT_FOUND, "계정을 찾을 수 없습니다.", null);
		}

		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(sessionDto.getData().getClientId(), sessionDto.getUsername());
		if (tokens.isEmpty()) {
			return new ResponseBean<>(StatusType.NOT_FOUND, "발급 된 Token이 없습니다.", null);
		}

		String tokenId = extractTokenKey(tokens.stream().findFirst().orElse(null).getValue());

		LoginHistory loginHistory
				= LoginHistory
				.builder()
				.tokenId(tokenId)
				.ipAddress(NetworkUtils.getClientIpAddress(request))
				.loginType(LoginType.LOGOUT)
				.logAt(LocalDateTime.now())
				.user(user)
//						  .company()
				.build();

		loginHistoryService.register(loginHistory);

		// 발급 한 토큰 삭제
		tokens.forEach(
				token -> {
					tokenStore.removeRefreshToken(token.getRefreshToken());
					tokenStore.removeAccessToken(token);
				}
		);

		SessionDto session = sessionMapper.toDto(user);
		session.setClientId(sessionDto.getData().getClientId());

		return new ResponseBean<>(StatusType.OK, session);
	}


	protected String extractTokenKey(String value) {
		if (value == null) {
            return null;
        }
		MessageDigest digest;
		try {
            digest = MessageDigest.getInstance("MD5");
        }
		catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

		byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
		return String.format("%032x", new BigInteger(1, bytes));
	}

	@PostMapping(value = "/loginHistory")
	@Operation(description = "로그인 기록을 저장합니다.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "session dto", required = true))
	public ResponseBean<SessionDto> loginHistory(@RequestBody @Valid RequestBean<SessionDto> sessionDto, HttpServletRequest request){

		User user = userService.findUserByUsername(sessionDto.getUsername());

		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(sessionDto.getData().getClientId(), sessionDto.getUsername());

		if (null == user) {
			return new ResponseBean<>(StatusType.NOT_FOUND, "계정을 찾을 수 없습니다.", null);
		}

		if (tokens.isEmpty()) {
			return new ResponseBean<>(StatusType.NOT_FOUND, "발급 된 Token이 없습니다.", null);
		}

		LoginHistory loginHistory
				= LoginHistory
				.builder()
				.tokenId(extractTokenKey(tokens.stream().findFirst().orElse(null).getValue()))
				.ipAddress(NetworkUtils.getClientIpAddress(request))
				.loginType(LoginType.LOGIN)
				.logAt(LocalDateTime.now())
				.user(user)
//						  .company()
				.build();

		loginHistoryService.register(loginHistory);

		return new ResponseBean<>(StatusType.OK);
	}
}
