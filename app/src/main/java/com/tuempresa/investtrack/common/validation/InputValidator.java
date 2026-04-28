package com.tuempresa.investtrack.common.validation;

public final class InputValidator {

    private InputValidator() {
    }

    public static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    public static boolean hasText(String value) {
        return !normalize(value).isEmpty();
    }

    public static boolean isValidEmail(String email) {
        String normalizedEmail = normalize(email);
        return normalizedEmail.contains("@") && normalizedEmail.contains(".");
    }
}
