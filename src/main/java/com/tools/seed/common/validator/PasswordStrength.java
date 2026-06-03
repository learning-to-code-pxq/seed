package com.tools.seed.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordStrengthValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordStrength {
    String message() default "密码必须6-20位，包含字母和数字";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}