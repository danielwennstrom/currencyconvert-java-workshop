package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TargetCurrency {
    public TargetCurrency() {
        toBase = "";
        displayName = "";
        symbol = "";
        rateFromBase = 0.0;
        lastUpdated = LocalDateTime.now();
    }

    @JsonProperty("toBase")
    private String toBase;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("rateFromBase")
    private double rateFromBase;

    @JsonProperty("lastUpdated")
    private LocalDateTime lastUpdated;

    public String getToBase() {
        return toBase;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getRateFromBase() {
        return rateFromBase;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getFormattedLastUpdated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss 'on' yyyy-MM-dd");
        return lastUpdated.format(formatter);
    }
}
