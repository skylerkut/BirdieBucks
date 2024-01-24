package com.example.as1.Controllers.RecycleViews;
public class StockPreviewScrollCard {
    private long id;
    private double currValue;
    private String company;

    public StockPreviewScrollCard(String stockName, double currValue, long id) {
        this.company = stockName;
        this.currValue = currValue;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCurrValue() {
        return currValue;
    }

    public void setCurrValue(double currValue) {
        this.currValue = currValue;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

}