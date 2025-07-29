package com.innocito.axcl.config.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class XSSRequestWrapper extends HttpServletRequestWrapper {

    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    private List<String> ignoreFields = List.of();

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (ignoreFields.contains(name))
            return sanitize(value);
        else return sanitizeParticularFields(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }

        if (ignoreFields.contains(name))
            return Arrays.stream(values)
                    .map(this::sanitizeParticularFields)
                    .toArray(String[]::new);
        else return Arrays.stream(values)
                .map(this::sanitize)
                .toArray(String[]::new);
    }

    private String sanitize(String input) {
        return input == null ? null : Jsoup.clean(input, Safelist.basic());
    }

    private String sanitizeParticularFields(String input) {
        if (input == null) {
            return null;
        }

        // Replace consecutive spaces with a placeholder, e.g., "&nbsp;" to preserve them
        String preservedSpacesInput = input.replaceAll(" {2,}", "&nbsp;&nbsp;");
        String sanitizedInput = Jsoup.clean(preservedSpacesInput, Safelist.basic());
        return sanitizedInput.replaceAll("&nbsp;&nbsp;", "  ");
    }
}