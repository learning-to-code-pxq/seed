package com.tools.seed.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {

    // 至少包含一个字母和一个数字，6-20位
    private static final String PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(password)) return false;
        return password.matches(PATTERN);
    }
}