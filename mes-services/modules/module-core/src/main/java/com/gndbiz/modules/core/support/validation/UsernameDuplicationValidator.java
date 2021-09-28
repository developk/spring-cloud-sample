package com.gndbiz.modules.core.support.validation;

import com.gndbiz.modules.core.domain.user.UserService;
import com.gndbiz.modules.core.support.validation.annotations.UsernameUnique;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
public class UsernameDuplicationValidator implements ConstraintValidator<UsernameUnique, String> {

	private final UserService userService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (userService.existsUserByUsername(value)) {
			context.disableDefaultConstraintViolation();;
			context.buildConstraintViolationWithTemplate(
					MessageFormat.format("계정: {0} 은 이미 존재하는 계정입니다.", value)
			)
					.addConstraintViolation()
			;
			return false;
		}

		return true;

	}

	@Override
	public void initialize(UsernameUnique constraintAnnotation) {
	}
}
