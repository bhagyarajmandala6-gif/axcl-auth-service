package com.innocito.axcl.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CustomPostalCodeValidator implements ConstraintValidator<CustomValidPostalCode, String> {

    private static final String POSTAL_CODE_REGEX = "^[a-zA-Z0-9 -]{3,10}$"; //  Allows 12345, 90210, A1B 2C3, W1A 1AA.

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Let @NotBlank handle empty cases
        }
        return value.matches(POSTAL_CODE_REGEX);
    }
}
