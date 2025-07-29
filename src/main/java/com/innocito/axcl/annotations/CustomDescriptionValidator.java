package com.innocito.axcl.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CustomDescriptionValidator implements ConstraintValidator<CustomValidDescription, String> {

    private static final String DESCRIPTION_REGEX = "^[a-zA-Z0-9 .,!?'\"\\n\\r()@#$%^&*-_+=/\\\\]{5,500}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Let @NotBlank handle empty cases
        }
        return value.matches(DESCRIPTION_REGEX);
    }
}
