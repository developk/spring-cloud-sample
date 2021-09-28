package com.gndbiz.modules.core.support.handler;

import com.gndbiz.commons.date.DateUtils;
import com.gndbiz.modules.core.support.enums.StatusType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * access token 만료 체크
 */

@Component
@Slf4j
public class GndWebResponseTranslator extends DefaultWebResponseExceptionTranslator {

	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {

		ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
		OAuth2Exception auth2Exception = responseEntity.getBody();

		Map<String, Object> responseMap = new HashMap<>();

		if (auth2Exception != null) {

			int code;
			String codeMessage;

			String exceptionMessage = auth2Exception.getMessage();

			if (exceptionMessage.contains("Access token expired:") || exceptionMessage.contains("Token has expired")) {
				code = StatusType.EXPIRED_TOKEN.getCode();
				codeMessage = StatusType.EXPIRED_TOKEN.getDesc();

			} else if ("unauthorized".equals(auth2Exception.getOAuth2ErrorCode())) {
				code = StatusType.UNAUTHORIZED.getCode();
				codeMessage = StatusType.UNAUTHORIZED.getDesc();
			} else {
				code = StatusType.INVALID_TOKEN.getCode();
				codeMessage = StatusType.INVALID_TOKEN.getDesc();
			}

			log.error("Status: {}, error: {}, message: {} auth2error: {}", code, exceptionMessage, codeMessage, auth2Exception.getOAuth2ErrorCode());

			responseMap.put("statusCode", code);
			responseMap.put("timestamp", DateUtils.getFormattedDateStr());
			responseMap.put("message", codeMessage);
			responseMap.put("error", exceptionMessage);
			responseMap.put("data", null);
		}

		return new ResponseEntity(responseMap, HttpStatus.UNAUTHORIZED);
	}

}
