package com.gndbiz.modules.core.support.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 오류 코드 유형 이넘 클래스
 */
@Getter
@RequiredArgsConstructor
public enum StatusType {


	// 정상
	OK(200, "성공"),


	// 요청 측 오류
	BAD_REQUEST(400, "비 정상적인 요청입니다."),
	NOT_FOUND(400, "자원을 찾을 수 없습니다."),
	METHOD_NOT_ALLOWED(405, "지원하지 않는 HTTP METHOD 입니다."),
	UNAUTHORIZED(401, "인증되지 않은 요청입니다(인증 토큰 헤더를 확인해주세요)."),
	INVALID_TOKEN(601, "유효하지 않은 토큰입니다."),
	EXPIRED_TOKEN(602, "토큰이 만료되었습니다."),

	
	// 서버 측 오류
	INTERNAL_SERVER_ERROR(500, "처리 중 오류가 발생했습니다."),
	VALIDATION_FAIL(701, "유효성 검증에 실패하엿습니다.")

	;

	private static final StatusType[] VALUES;

	static {
		VALUES = values();
	}

	private final int code;
	private final String desc;

	public static StatusType valueOf(int code) throws IllegalAccessException {
		StatusType statusType = resolve(code);
		if (null == statusType) {
			throw new IllegalAccessException("매칭되는 상태코드가 없습니다. ["+code+"]");
		}
		return statusType;
	}

	@Nullable
	public static StatusType resolve(int code) {
		for (StatusType type: VALUES) {
			if (type.code == code) {
				return type;
			}
		}
		return null;
	}

	public int value() {
		return this.code;
	}
}
