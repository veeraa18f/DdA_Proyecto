package com.tuempresa.investtrack.data.model;

public class Asset {

    private final String id;
    private final String name;
    private final String ticker;
    private final String type;
    private final double currentPrice;
    private final double quantity;
    private final double averagePrice;
    private final String logoDrawableName;

    public Asset(
            String id,
            String name,
            String ticker,
            String type,
            double currentPrice,
            double quantity,
            double averagePrice,
            String logoDrawableName
    ) {
        this.id = id;
        this.name = name;
        this.ticker = ticker;
        this.type = type;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.logoDrawableName = logoDrawableName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTicker() {
        return ticker;
    }

    public String getType() {
        return type;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public String getLogoDrawableName() {
        return logoDrawableName;
    }

    public double getCurrentValue() {
        return currentPrice * quantity;
    }

    public double getProfit() {
        return (currentPrice - averagePrice) * quantity;
    }
}
