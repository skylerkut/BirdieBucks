package com.example.as1.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    //Global User
    private static User instance = null;
    public static User getInstance(){
        if (instance == null){
            instance = new User(-1,-1,"NoInputs", "NoInputs", "NoInputs", "NoInputs", "NoInputs", 'u');
        }
        return instance;
    }
    public static User updateInstance(User user){
        instance = user;
        return instance;
    }

    private List<StockPurchased> stocks = new ArrayList<>();
    //Params
    private long id;
    private double money;
    private String name;
    private String email;
    private String dob;
    private String username;
    private String password;
    protected char priv;
    private FriendGroup friendGroup;

//Constructors
    public User(long id, double money, String name, String email, String dob, String username, String password, char priv){
        this.id = id;
        this.money = money;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.username = username;
        this.password = password;
        this.priv = priv;
    }
    public User(){}

    //Getters and Setters
    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }
    public double getMoney(){  return money; }
    public void setMoney(double money){   this.money = money; }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getUsername() {   return username; }

    public void setUsername(String user){
        this.username = user;
    }

    public String getPassword() {   return password; }

    public void setPassword(String pass){
        this.password = pass;
    }

    public String getDob(){ return dob; }

    public void setDob(String dob){ this.dob = dob; }

    public int getNumStocksPurchased(){return stocks.size() - 1;}

    public char getPrivilege(){return this.priv;}

    public void setPrivilege(char privilege){this.priv = privilege;}

    public FriendGroup getFriendGroup() {
        return friendGroup;
    }

    public void setFriendGroup(FriendGroup friendGroup) {
        this.friendGroup = friendGroup;
    }



    public StockPurchased setStock(Stock stock, int numPurchase, Long id){
        for(int i = 0; i <  stocks.size(); ++i){
            if(stock.getId().equals(stocks.get(i).getStock().getId()) && money >= numPurchase * stock.getCurrValue()){
                StockPurchased toRemove = stocks.get(i);
                stocks.get(i).setNumPurchased(stocks.get(i).getNumPurchased() + numPurchase);
                stocks.get(i).setCostPurchase(stocks.get(i).getCostPurchase() + stocks.get(i).getStock().getCurrValue() * numPurchase);
                return toRemove;
            }
        }
        StockPurchased curr = new StockPurchased();
        curr.setStock(stock);
        curr.setUser(this);
        curr.setId(id);
        curr.setNumPurchased(numPurchase);
        curr.setSinglePrice(stock.getCurrValue());
        curr.setCostPurchase(curr.getNumPurchased() * stock.getCurrValue());

        if( curr.getNumPurchased() * stock.getCurrValue() > money) return null;
        stocks.add(curr);
        stock.setUser(this, numPurchase, id);
        money -= curr.getCostPurchase();
        return curr;
    }

    public StockPurchased removeStocks(int numStocks, Stock stock){
        StockPurchased potentiallyRemoved;
        for(int i = 0; i < stocks.size(); ++i){
            if(stock.getId().equals(stocks.get(i).getStock().getId())){
                if(numStocks >= stocks.get(i).getNumPurchased()){
                    double moneyChanged = stocks.get(i).getCostPurchase();
                    money += moneyChanged;
                    potentiallyRemoved = stocks.get(i);
                    stocks.remove(stocks.get(i));
                    return potentiallyRemoved;
                }else{
                    double moneyChanged = stocks.get(i).getSinglePrice() * numStocks;
                    money += moneyChanged;
                    potentiallyRemoved = stocks.get(i);
                    stocks.get(i).setNumPurchased(stocks.get(i).getNumPurchased() - numStocks);
                    stocks.get(i).setCostPurchase(stocks.get(i).getCostPurchase() - numStocks * stocks.get(i).getSinglePrice());
                    return potentiallyRemoved;
                }
            }
        }
        return null;
    }

    public List<StockPurchased> getStocks(){return stocks;}

    public void setStocks(List<StockPurchased> stocksIn){this.stocks = stocksIn;}

    public StockPurchased purchase(int numStocks, Stock stock, long id){
        if(numStocks < 1 || stock == null) return null;
        return setStock(stock, numStocks, id);
    }

    public StockPurchased sell(int numStocks, Stock stock){
        if(numStocks < 1 || stock == null) return null;
        return removeStocks(numStocks, stock);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StockPurchased other = (StockPurchased) obj;
        if (Objects.equals(id, null)) {
            return other.getId() == null;
        } else return Objects.equals(id, other.getId());
    }



}//end

