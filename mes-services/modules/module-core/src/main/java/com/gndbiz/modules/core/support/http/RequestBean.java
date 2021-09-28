package com.gndbiz.modules.core.support.http;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gndbiz.modules.core.support.validation.annotations.UserExists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 *
 * @param <F> Form
 */
@Getter
@Setter
@Schema(
		description = "요청 전달 객체"
)
public class RequestBean<F> {

	@Schema(description = "요청시간", type = "string", hidden = true, format = "date-time", example = "2021-09-08 14:11:22")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;

	@Schema(description = "API 요청 계정", required = true)
	@NotBlank(message = "요청한 사용자 계정을 포함해야 합니다.")
	@UserExists
	private String username;

	@Schema(description = "요청 데이터", required = true)
	private @NotNull(message = "데이터가 없습니다.") @Valid F data;

	public RequestBean() {
		this.timestamp = LocalDateTime.now();
	}
}
