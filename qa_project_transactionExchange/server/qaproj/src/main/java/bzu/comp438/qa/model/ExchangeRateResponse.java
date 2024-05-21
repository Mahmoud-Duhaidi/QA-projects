package bzu.comp438.qa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ExchangeRateResponse {

    @JsonProperty("conversion_rates") // Adjust the string to match the actual JSON field name
    private Map<String, Double> conversionRates;
    // Getters and setters

    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }

    public void setConversionRates(Map<String, Double> conversion_rates) {
        this.conversionRates = conversion_rates;
    }
}
