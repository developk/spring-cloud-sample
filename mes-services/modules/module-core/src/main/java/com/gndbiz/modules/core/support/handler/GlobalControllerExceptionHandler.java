package com.gndbiz.modules.core.support.handler;

import com.gndbiz.modules.core.support.enums.StatusType;
import com.gndbiz.modules.core.support.http.ResponseBean;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

	// region CLIENT SIDE ERRORS HANDLING

	// 400
	@ExceptionHandler(ConversionFailedException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "잘못된 요청입니다.")
	protected ResponseBean<?> handleBadRequest(RuntimeException e) {
		return new ResponseBean<>(StatusType.BAD_REQUEST, e.getMessage(), null);
	}

	// 404
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "자원을 찾을 수 없습니다.")
	protected ResponseBean<?> handleResourceNotFound(NoHandlerFoundException e) {
		return new ResponseBean<>(StatusType.NOT_FOUND, e.getMessage(), null);
	}

	// 405
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED, reason = "허용되지 않은 Http Method 요청 입니다.")
	protected ResponseBean<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
		return new ResponseBean<>(StatusType.METHOD_NOT_ALLOWED, e.getMessage(), null);
	}

	// endregion

	// region SERVER SIDE ERRORS HANDLING

	// 500
	@ExceptionHandler(Exception.class)
	protected ResponseBean<?> handleInternalServerException(Exception e) {
		return new ResponseBean<>(StatusType.INTERNAL_SERVER_ERROR, e.getMessage(), null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseBean<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

		String message = e.getMessage();

		if (e.getBindingResult().hasErrors()) {
			message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		}

		return new ResponseBean<>(StatusType.VALIDATION_FAIL, message, null);
	}

	// endregion

}
