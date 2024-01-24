package com.example.as1.Controllers.RecycleViews;

public class UserScrollCard {

    private long id;
    private String username;
    private char permission;

    public UserScrollCard(String name, long id, char perm){
        this.username = name;
        this.id = id;
        this.permission = perm;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char getPermission() {
        return permission;
    }

    public void setPermission(char permission) {
        this.permission = permission;
    }

}
