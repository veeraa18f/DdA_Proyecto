package InvestTrack.utils;

public final class PortfolioMetrics {

    private final double marketValue;
    private final double profit;
    private final double returnPercentage;

    private PortfolioMetrics(double marketValue, double profit, double returnPercentage) {
        this.marketValue = marketValue;
        this.profit = profit;
        this.returnPercentage = returnPercentage;
    }

    public static PortfolioMetrics from(double currentPrice, double quantity, double averagePrice) {
        double marketValue = currentPrice * quantity;
        double costBasis = averagePrice * quantity;
        double profit = marketValue - costBasis;
        double returnPercentage = costBasis == 0 ? 0 : (profit / costBasis) * 100;

        return new PortfolioMetrics(marketValue, profit, returnPercentage);
    }

    public double getMarketValue() {
        return marketValue;
    }

    public double getProfit() {
        return profit;
    }

    public double getReturnPercentage() {
        return returnPercentage;
    }
}
