package com.example.as1.Controllers.RecycleViews;

public class StockScrollCard {

    private int imageView;
    private int numPurchased;
    private double stockPrice;
    private String stockName;
    private int id;
    private String symbol;

    public StockScrollCard(String stockName, int numPurchased, double stockPrice, int id, String symbol){
        this.stockName = stockName;
        this.numPurchased = numPurchased;
        this.stockPrice = stockPrice;
        this.id = id;
        this.symbol = symbol;

    }

    public int getNumPurchased() {
        return numPurchased;
    }

    public void setNumPurchased(int numPurchased) {
        this.numPurchased = numPurchased;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(int stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

}
