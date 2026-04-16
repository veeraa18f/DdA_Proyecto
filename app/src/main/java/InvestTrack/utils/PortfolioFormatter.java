package InvestTrack.utils;

import java.text.NumberFormat;
import java.util.Locale;

public final class PortfolioFormatter {

    private PortfolioFormatter() {
    }

    public static String formatCurrency(double value) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(value);
    }

    public static String formatSignedCurrency(double value) {
        return (value >= 0 ? "+" : "-") + formatCurrency(Math.abs(value));
    }

    public static String formatSignedPercent(double value) {
        return String.format(Locale.US, "%+.2f%%", value);
    }

    public static String formatQuantity(double quantity) {
        if (Math.floor(quantity) == quantity) {
            return String.format(Locale.US, "%.0f shares", quantity);
        }

        return String.format(Locale.US, "%.2f shares", quantity);
    }
}
