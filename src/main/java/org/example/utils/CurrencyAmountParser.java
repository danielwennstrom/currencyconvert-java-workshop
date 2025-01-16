package org.example.utils;

public class CurrencyAmountParser {
    public static double formatAmount(String amountString) {
        String cleaned = amountString.replace(" ", "");

        // pattern tries to match the first group of 1-3 digits and any following groups of three digits
        // as having thousands separators. if so, they are removed and won't need to be parsed further.
        if (cleaned.matches("\\d{1,3}([.,]\\d{3})*")) {
            return Double.parseDouble(cleaned.replaceAll("[.,]", ""));
        }

        // otherwise, the method parses through the string and tries to handle thousands and decimal values
        // in the two common formats (1.234,56, 1,234.56) and some odd edge cases.
        int lastComma = cleaned.lastIndexOf(',');
        int lastDot = cleaned.lastIndexOf('.');
        boolean hasCommaDecimal = cleaned.contains(",") && lastComma > lastDot;
        boolean hasDotDecimal = cleaned.contains(".") && lastComma < lastDot;

        int dotCount = (int) cleaned.chars().filter(ch -> ch == '.').count();
        if (dotCount >= 1 && hasCommaDecimal) {
            lastComma = cleaned.lastIndexOf(',');
            cleaned = cleaned.substring(0, lastComma).replace(".", "")
                    + cleaned.substring(lastComma).replace(",", ".");
        } else if (dotCount >= 1) {
            lastDot = cleaned.lastIndexOf('.');
            cleaned = cleaned.substring(0, lastDot).replace(".", "")
                    + cleaned.substring(lastDot);
        }

        int commaCount = (int) cleaned.chars().filter(ch -> ch == ',').count();
        if (commaCount >= 1 && hasDotDecimal) {
            lastDot = cleaned.lastIndexOf('.');
            cleaned = cleaned.substring(0, lastDot).replace(",", "")
                    + cleaned.substring(lastDot);
        } else if (commaCount >= 1) {
            lastComma = cleaned.lastIndexOf(',');
            cleaned = cleaned.substring(0, lastComma).replaceAll(",", "")
                    + cleaned.substring(lastComma).replace(",", ".");
        }

        return Double.parseDouble(cleaned);
    }
}
