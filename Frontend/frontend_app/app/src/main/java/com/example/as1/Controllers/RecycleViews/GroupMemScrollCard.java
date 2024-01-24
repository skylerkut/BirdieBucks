package com.example.as1.Controllers.RecycleViews;

public class GroupMemScrollCard {



    protected String name;
    private String username;
    private int id;

    private char permission;
    protected double money;
    protected int numStocks;

    public GroupMemScrollCard(String name, String username, int id, char perm, double money, int num){
        this.username = username;
        this.id = id;
        this.permission = perm;
        this.name = name;
        this.money = money;
        this.numStocks = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getPermission() {
        return permission;
    }

    public void setPermission(char permission) {
        this.permission = permission;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getNumStocks() {
        return numStocks;
    }

    public void setNumStocks(int numStocks) {
        this.numStocks = numStocks;
    }

}
