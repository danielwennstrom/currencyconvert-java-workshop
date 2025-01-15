package org.example.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {
    public static String formatCurrency(String currencySymbol, double amount) {
        Locale locale = getLocaleForCurrencySymbol(currencySymbol);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        return currencyFormat.format(amount);
    }

    public static double formatAmount(String amountString) {
        String cleaned = amountString.replace(" ", "");

        boolean isCommaDecimal = false;
        int lastComma = cleaned.lastIndexOf(',');
        int lastDot = cleaned.lastIndexOf('.');

        if (lastComma > lastDot) {
            isCommaDecimal = true;
        }

        if (isCommaDecimal) {
            cleaned = cleaned.replace(".", "");
            cleaned = cleaned.replace(",", ".");
        } else {
            cleaned = cleaned.replace(",", "");
        }

        int dotCount = (int) cleaned.chars().filter(ch -> ch == '.').count();
        if (dotCount > 1) {
            int lastDotIndex = cleaned.lastIndexOf('.');
            cleaned = cleaned.substring(0, lastDotIndex).replace(".", "") + cleaned.substring(lastDotIndex);
        }

        return Double.parseDouble(cleaned);
    }

    public static Locale getLocaleForCurrencySymbol(String currencySymbol) {
        return switch (currencySymbol) {
            case "kr" -> new Locale.Builder()
                    .setLanguage("sv")
                    .setRegion("SE")
                    .build();
            case "$" -> Locale.US;
            case "€" -> Locale.GERMANY;
            case "£" -> Locale.UK;
            case "¥" -> Locale.CHINA;
            case "₽" -> new Locale.Builder()
                    .setLanguage("ru")
                    .setRegion("RU")
                    .build();
            default -> throw new IllegalArgumentException("Unsupported currency symbol: " + currencySymbol);
        };
    }
}
