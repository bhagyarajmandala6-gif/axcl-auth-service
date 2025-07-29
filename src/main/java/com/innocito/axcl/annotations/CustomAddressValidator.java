package com.innocito.axcl.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CustomAddressValidator implements ConstraintValidator<CustomValidAddress, String> {

    private static final String ADDRESS_REGEX = "^[a-zA-Z0-9 .,-]{5,100}$"; // Supports standard addresses like 123 Main St., New York, NY.

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Let @NotBlank handle empty cases
        }
        return value.matches(ADDRESS_REGEX);
    }
}
