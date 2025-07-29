package com.innocito.axcl.annotations;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.innocito.axcl.util.ApplicationConstants.INDIA_CODE;

public class CustomMobileNumberValidator implements ConstraintValidator<CustomValidMobileNumber, String> {

    private static final String MOBILE_REGEX = "^\\+?[0-9]{7,15}$"; // Supports international numbers

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Let @NotBlank handle empty cases
        } else {
            if (!isValidMobileNumber(value)) {
                return false;
            }
        }

        return value.matches(MOBILE_REGEX);
    }

    private boolean isValidMobileNumber(String mobileNumber) {
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            // Assuming default region as "IN" (India) — change as per your needs
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(mobileNumber, INDIA_CODE);
            return phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }
}