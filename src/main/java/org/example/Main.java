package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.BaseCurrency;
import org.example.utils.CurrencyAmountParser;
import org.example.utils.CurrencyConverter;
import org.example.models.TargetCurrency;
import org.example.utils.CurrencyFormatter;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        List<BaseCurrency> currencies = objectMapper.readValue(new File("src/main/java/org/example/data/data.json"),
                new TypeReference<>() {
                });
        Scanner scanner = new Scanner(System.in);

        while (true) {
            BaseCurrency baseCurrency = getBaseCurrency(currencies, scanner);
            if (baseCurrency == null) {
                break;
            }

            double amount = getAmount(baseCurrency, scanner);
            if (amount == -1) {
                continue;
            }

            TargetCurrency targetCurrency = getTargetCurrency(baseCurrency, amount, scanner);
            if (targetCurrency == null) {
                continue;
            }

            convertCurrency(baseCurrency, amount, targetCurrency);
            break;
        }
    }

    private static BaseCurrency getBaseCurrency(List<BaseCurrency> currencies, Scanner scanner) {
        BaseCurrency selectedCurrency = new BaseCurrency();
        int choice;

        System.out.println("Enter currency option to convert from: ");
        System.out.println("Available options");
        for (int i = 0; i < currencies.size(); i++) {
            BaseCurrency currency = currencies.get(i);
            System.out.printf("%s. %s (%s)%n", i + 1,
                    currency.getDisplayName(), currency.getFromBase());
        }
        System.out.println("0. Exit program");
        System.out.println("---");

        try {
            choice = scanner.nextInt();
            if (choice >= currencies.size() + 1 || choice < 0) {
                System.out.println("Invalid option, enter a valid number.");
            } else if (choice == 0) {
                System.out.println("Exiting.");
                return null;
            } else {
                selectedCurrency = currencies.get(choice - 1);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input: " + e.getMessage());
            scanner.nextLine();
        }

        return selectedCurrency;
    }

    private static double getAmount(BaseCurrency baseCurrency, Scanner scanner) {
        double amount;

        System.out.printf("Enter the amount to convert from %s:%n",
                baseCurrency.getFromBase());

        try {
            scanner.nextLine();
            String amountString = scanner.nextLine();
            amount = CurrencyAmountParser.formatAmount(amountString);

            if (amount <= 0) {
                System.out.println("The amount can't be negative, try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input: " + e.getMessage());
            scanner.nextLine();
            return -1;
        }

        return amount;
    }

    private static TargetCurrency getTargetCurrency(BaseCurrency baseCurrency, double amount, Scanner scanner) {
        List<TargetCurrency> availableCurrencies = baseCurrency.getAvailableCurrencies();
        TargetCurrency selectedCurrency;
        int choice;

        if (availableCurrencies.isEmpty()) {
            System.out.printf("There are no available currencies to convert %s into.%n", baseCurrency.getFromBase());
            System.out.println("---");
            return null;
        } else {
            System.out.printf("Enter currency option to convert %s into: %n",
                    CurrencyFormatter.formatCurrency(baseCurrency.getSymbol(), amount));
            System.out.println("Available options");

            for (int i = 0; i < availableCurrencies.size(); i++) {
                TargetCurrency currency = availableCurrencies.get(i);
                System.out.printf("%s. %s (%s)%n", i + 1,
                        currency.getDisplayName(), currency.getToBase());
            }
            System.out.println("---");

            try {
                choice = scanner.nextInt();
                if (choice >= availableCurrencies.size() + 1 || choice <= 0) {
                    System.out.println("Invalid option, enter a valid number.");
                    return null;
                } else {
                    selectedCurrency = availableCurrencies.get(choice - 1);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input: " + e.getMessage());
                scanner.nextLine();
                return null;
            }

            return selectedCurrency;
        }
    }

    private static void convertCurrency(BaseCurrency baseCurrency, double amount, TargetCurrency targetCurrency) {
        System.out.printf("Converting %s to %s, at a rate of %s %s = %s %s as of %s: %n",
                CurrencyFormatter.formatCurrency(baseCurrency.getSymbol(), amount),
                targetCurrency.getSymbol(),
                "1",
                baseCurrency.getFromBase(),
                targetCurrency.getRateFromBase(),
                targetCurrency.getToBase(),
                targetCurrency.getFormattedLastUpdated());

        double convertedAmount = CurrencyConverter.convertCurrency(amount, targetCurrency.getRateFromBase());
        System.out.printf("%s%n", CurrencyFormatter.formatCurrency(targetCurrency.getSymbol(), convertedAmount));
        System.out.println("---");
    }
}