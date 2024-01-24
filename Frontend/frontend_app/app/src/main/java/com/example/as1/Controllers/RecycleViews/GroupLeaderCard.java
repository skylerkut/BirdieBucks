package com.example.as1.Controllers.RecycleViews;

public class GroupLeaderCard {


    protected String name;
    private String username;
    private int id;
    private char permission;

    public GroupLeaderCard(String name, String username, int id, char permission) {
        this.name = name;
        this.username = username;
        this.id = id;
        this.permission = permission;
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

}
