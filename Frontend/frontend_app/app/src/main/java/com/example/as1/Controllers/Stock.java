package com.example.as1.Controllers;

import java.util.ArrayList;
import java.util.List;

public class Stock {

    private Long id;
    private String symbol;
    private String company;
    private double currValue;
    private double prevDayChange;
    private List<StockPurchased> users = new ArrayList<>();

    public Stock(Long id, String symbol, String company, double currValue, double prevDayChange) {
        this.id = id;
        this.symbol = symbol;
        this.company = company;
        this.currValue = currValue;
        this.prevDayChange = prevDayChange;
    }

    public Stock() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getCurrValue() {
        return currValue;
    }

    public void setCurrValue(double currValue) {
        this.currValue = currValue;
    }

    public double getPrevDayChange() {
        return prevDayChange;
    }

    public void setPrevDayChange(double prevDayChange) {
        this.prevDayChange = prevDayChange;
    }

    public void setUser(User user, int numBuying, Long id) {
        StockPurchased curr = new StockPurchased();
        curr.setId(id);
        curr.setUser(user);
        curr.setStock(this);
        curr.setSinglePrice(currValue);
        curr.setNumPurchased(numBuying);
        curr.setCostPurchase(numBuying * currValue);
        users.add(curr);
    }

    public List<StockPurchased> getUsers() {
        return users;
    }


}//end
