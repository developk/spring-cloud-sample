package com.gndbiz.modules.core.support.validation;

import com.gndbiz.modules.core.domain.user.UserService;
import com.gndbiz.modules.core.support.validation.annotations.UserExists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
public class UserExistsValidator implements ConstraintValidator<UserExists, String > {

	private final UserService userService;
	
	@Override
	public void initialize(UserExists constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if (userService.existsUserByUsername(value)) {
			return true;
		}

		context.disableDefaultConstraintViolation();;
		context.buildConstraintViolationWithTemplate(
						MessageFormat.format("계정: {0}은 존재하지 않는 계정입니다.", value)
				)
				.addConstraintViolation()
		;
		return false;
	}
}
