package com.innocito.axcl.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BasicUtils {
    private final MessageSource messageSource;

    public String getLocalizedMessage(String key, Object[] objects) {
        return messageSource.getMessage(key, objects, LocaleContextHolder.getLocale());
    }

    public String maskMobileNumber(String mobileNumber) {
        if (StringUtils.isBlank(mobileNumber)) {
            return mobileNumber;
        }
        String maskedNumber = mobileNumber
                .substring(mobileNumber.indexOf("-") + 1).replaceAll(".(?=.{4})", "*");
        return mobileNumber.substring(0, mobileNumber.indexOf("-") + 1) + maskedNumber;
    }

    public String maskEmail(String email) {
        if (Strings.isBlank(email)) {
            return null;
        }
        // Split the email into local part (before @) and domain part (after @)
        String[] emailParts = email.split("@");
        String localPart = emailParts[0];
        String domainPart = emailParts[1];

        // Mask the local part by keeping the first and last characters visible
        if (localPart.length() <= 2) {
            // If the local part is too short, mask all but the first character
            localPart = localPart.charAt(0) + "*".repeat(localPart.length() - 1);
        } else {
            // Mask all but the first and last characters
            localPart = localPart.charAt(0) + "*".repeat(localPart.length() - 2) + localPart.charAt(localPart.length() - 1);
        }

        // Return the masked email
        return localPart + "@" + domainPart;
    }
}