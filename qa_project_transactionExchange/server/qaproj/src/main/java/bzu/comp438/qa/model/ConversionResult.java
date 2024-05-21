package bzu.comp438.qa.model;

public class ConversionResult {
    private double convertedAmount;

    public ConversionResult(double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}