package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.BaseCurrency;
import org.example.models.TargetCurrency;
import org.example.utils.Converter;
import org.example.utils.Formatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    @DisplayName("Basic conversion")
    void conversion() {
        double amount = 100;
        double rate = 0.089;

        assertEquals(8.9, Converter.convertCurrency(amount, rate));
    }

    @Test
    @DisplayName("Currency formatting")
    void formatCurrency() {
        String resultA = Formatter.formatCurrency("kr", 1234.56);
        String resultB = Formatter.formatCurrency("$", 1234.56);

        assertAll(
                () -> assertEquals("1 234,56 kr", resultA.replace('\u00A0', ' ')),
                () -> assertEquals("$1,234.56", resultB.replace('\u00A0', ' ')));
    }

    @Test
    @DisplayName("Amount formatting")
    void formatAmount() {
        double expected = 1234.56;
        String[] inputs = {
                "1,234.56",
                "1,234,56",
                "1.234.56",
                "1.234,56",
                "1 234.56",
                "1 234,56"
        };

        for (String input : inputs) {
            double result = Formatter.formatAmount(input);
            assertEquals(expected, result, "Failed for input: " + input);
        }
    }

    @Test
    @DisplayName("JSON deserialization")
    void deserialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        String json = """
                [
                      {
                        "fromBase": "SEK",
                        "displayName": "Swedish Krona",
                        "symbol": "kr",
                        "availableCurrencies": [
                          {
                            "toBase": "EUR",
                            "displayName": "Euro",
                            "symbol": "€",
                            "rateFromBase": 0.0868,
                            "lastUpdated": "2025-01-14T10:12:30"
                          }
                        ]
                    }
                ]
                """;

        List<BaseCurrency> currencies = objectMapper.readValue(json, new TypeReference<>() {
        });
        BaseCurrency currency = currencies.getFirst();
        TargetCurrency targetCurrency = currency.getAvailableCurrencies().getFirst();

        assertAll(
                () -> assertEquals("SEK", currency.getFromBase()),
                () -> assertEquals("Swedish Krona", currency.getDisplayName()),
                () -> assertEquals("kr", currency.getSymbol()),
                () -> assertEquals("EUR", targetCurrency.getToBase()),
                () -> assertEquals("Euro", targetCurrency.getDisplayName()),
                () -> assertEquals("€", targetCurrency.getSymbol()),
                () -> assertEquals(0.0868, targetCurrency.getRateFromBase()),
                () -> assertEquals("2025-01-14T10:12:30", targetCurrency.getLastUpdated().toString())
        );
    }

    @Test
    @DisplayName("LocalDateTime formatting")
    public void formatDate() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        String json = """
                [
                      {
                        "fromBase": "SEK",
                        "displayName": "Swedish Krona",
                        "symbol": "kr",
                        "availableCurrencies": [
                          {
                            "toBase": "EUR",
                            "displayName": "Euro",
                            "symbol": "€",
                            "rateFromBase": 0.0868,
                            "lastUpdated": "2025-01-14T10:12:30"
                          }
                        ]
                    }
                ]
                """;

        List<BaseCurrency> currencies = objectMapper.readValue(json, new TypeReference<>() {
        });
        BaseCurrency currency = currencies.getFirst();
        TargetCurrency targetCurrency = currency.getAvailableCurrencies().getFirst();

        assertEquals("10:12:30 on 2025-01-14", targetCurrency.getFormattedLastUpdated());
    }

    @Test
    @DisplayName("Get supported currency symbol")
    public void getSupportedSymbol() {
        Locale se = new Locale.Builder()
                .setLanguage("sv")
                .setRegion("SE")
                .build();

        assertAll(
                () -> assertEquals(Locale.US, Formatter.getLocaleForCurrencySymbol("$")),
                () -> assertEquals(se, Formatter.getLocaleForCurrencySymbol("kr"))
        );
    }

    @Test
    @DisplayName("Get unsupported currency symbol")
    public void getUnsupportedSymbol() {
        assertThrows(IllegalArgumentException.class, () -> Formatter.getLocaleForCurrencySymbol("Kč"));
    }
}