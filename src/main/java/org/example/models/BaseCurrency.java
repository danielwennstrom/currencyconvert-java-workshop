package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class BaseCurrency {

    public BaseCurrency() {
        fromBase = "";
        displayName = "";
        symbol = "";
        availableCurrencies = new ArrayList<>();
    }

    @JsonProperty("fromBase")
    private String fromBase;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("availableCurrencies")
    private ArrayList<TargetCurrency> availableCurrencies;

    public String getFromBase() {
        return fromBase;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    public ArrayList<TargetCurrency> getAvailableCurrencies() {
        return availableCurrencies;
    }

}
