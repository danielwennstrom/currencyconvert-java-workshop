package org.example.utils;

public class CurrencyAmountParser {
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
}
