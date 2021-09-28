package com.gndbiz.modules.core.support.validation.annotations;

import com.gndbiz.modules.core.support.validation.UsernameDuplicationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameDuplicationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameUnique {

	String message() default "사용자 계정은 중복될 수 없습니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
