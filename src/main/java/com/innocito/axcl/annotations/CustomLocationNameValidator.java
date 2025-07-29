package com.innocito.axcl.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CustomLocationNameValidator implements ConstraintValidator<CustomValidLocationName, String> {

    private static final String NAME_REGEX = "^[0-9a-zA-ZÀÁÂÃÄÅĀĂĄÇĆĈČĎÈÉÊËĒĔĖĘĚĜǴĞĠĢÌÍÎÏĨĪĬĮİĹĻĽŁÑŃŅŇÒÓÔÕÖŌŎŐŔŘŖŚŜŞŠŢŤŦÙÚÛÜŨŪŬŮŰŲŴÝŶŸŹŻŽàáâãäåāăąçćĉčďèéêëēĕėęěĝǵğġģìíîïĩīĭįıĺļľłñńņňòóôõöōŏőŕřŗśŝşšţťŧùúûüũūŭůűųŵýŷÿźżž'’.&\\- ]{2,50}$"; // Allows hyphens, spaces, apostrophes

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Let @NotBlank handle empty cases
        }
        return value.matches(NAME_REGEX);
    }
}
