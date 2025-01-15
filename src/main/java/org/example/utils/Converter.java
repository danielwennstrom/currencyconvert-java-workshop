package org.example.utils;

public class Converter {
    public static double convertCurrency(double amount, double rateFromBase) {
        return amount * rateFromBase;
    }
}
