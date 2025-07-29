package com.innocito.axcl.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CustomNameValidator implements ConstraintValidator<CustomValidName, String> {

    private int minLength;
    private int maxLength;
    private static final String NAME_REGEX = "^[a-zA-ZÀÁÂÃÄÅĀĂĄÇĆĈČĎÈÉÊËĒĔĖĘĚĜǴĞĠĢÌÍÎÏĨĪĬĮİĹĻĽŁÑŃŅŇÒÓÔÕÖŌŎŐŔŘŖŚŜŞŠŢŤŦÙÚÛÜŨŪŬŮŰŲŴÝŶŸŹŻŽàáâãäåāăąçćĉčďèéêëēĕėęěĝǵğġģìíîïĩīĭįıĺļľłñńņňòóôõöōŏőŕřŗśŝşšţťŧùúûüũūŭůűųŵýŷÿźżž'’.\\- ]+$"; // Allows hyphens, spaces, apostrophes


    @Override
    public void initialize(CustomValidName constraintAnnotation) {
        minLength = constraintAnnotation.minLength();
        maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Let @NotBlank handle empty cases
        }
        if (value.length() < minLength || value.length() > maxLength) {
            return false;
        }
        return value.matches(NAME_REGEX);
    }
}
