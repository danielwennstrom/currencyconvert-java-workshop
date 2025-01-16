package org.example.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String formatCurrency(String currencySymbol, double amount) {
        Locale locale = CurrencyLocaleHelper.getLocaleForCurrencySymbol(currencySymbol);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        return currencyFormat.format(amount);
    }
}
