package com.example.as1.Controllers;

public class StockPurchased {

    private Long id;
    private User user;
    private Stock stock;
    private int numPurchased;
    private double costPurchase;
    private double singlePrice;

    public StockPurchased() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public int getNumPurchased() {
        return numPurchased;
    }

    public void setNumPurchased(int numPurchased) {
        this.numPurchased = numPurchased;
    }

    public double getCostPurchase() {
        return costPurchase;
    }

    public void setCostPurchase(double costPurchase) {
        this.costPurchase = costPurchase;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(double singlePrice) {
        this.singlePrice = singlePrice;
    }


}
