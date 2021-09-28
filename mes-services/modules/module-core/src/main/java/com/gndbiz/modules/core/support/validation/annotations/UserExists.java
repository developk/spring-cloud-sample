package com.gndbiz.modules.core.support.validation.annotations;

import com.gndbiz.modules.core.support.validation.UserExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserExistsValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserExists {

	String message() default "계정이 존재하지 않습니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
