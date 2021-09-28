package com.gndbiz.modules.core.support.http;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gndbiz.modules.core.support.enums.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
		description = "응답 전달 객체"
)
public class ResponseBean<D> {

	@Schema(description = "응답코드")
	private int statusCode;

	@Schema(description = "메시지")
	private String message;

	@Schema(description = "오류 메시지")
	private String error;

	@Schema(description = "응답 데이터")
	private D data;

	@Schema(description = "응답시간", pattern = "yyyy-MM-dd HH:mm:ss", example = "2021-09-08 00:00:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;

	public ResponseBean() {
		this(StatusType.OK);
		this.timestamp = LocalDateTime.now();
	}

	public ResponseBean(StatusType statusType) {
		this.statusCode = statusType.value();
		this.message = statusType.getDesc();
		this.data = null;
		this.timestamp = LocalDateTime.now();
	}

	public ResponseBean(StatusType statusType, D data) {
		this.statusCode = statusType.value();
		this.message = statusType.getDesc();
		this.data = data;
		this.timestamp = LocalDateTime.now();
	}

	public ResponseBean(StatusType statusType, String message, D data) {
		this.statusCode = statusType.value();
		this.message = statusType.getDesc();
		this.error = message;
		this.data = data;
		this.timestamp = LocalDateTime.now();
	}

}
