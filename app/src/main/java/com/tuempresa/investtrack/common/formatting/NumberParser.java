package com.tuempresa.investtrack.common.formatting;

public final class NumberParser {

    private NumberParser() {
    }

    public static double parsePositiveDouble(String rawValue) {
        if (rawValue == null) {
            return -1;
        }
        try {
            return Double.parseDouble(rawValue.trim().replace(',', '.'));
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
