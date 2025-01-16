package org.example.utils;

public class CurrencyConverter {
    public static double convertCurrency(double amount, double rateFromBase) {
        return amount * rateFromBase;
    }
}
