package org.example.utils;

import java.util.Locale;

public class CurrencyLocaleHelper {
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
