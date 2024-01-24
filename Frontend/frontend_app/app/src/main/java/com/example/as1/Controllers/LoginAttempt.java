package com.example.as1.Controllers;

public class LoginAttempt {
    String username;
    String password;

    public LoginAttempt(){

    }
    public LoginAttempt(String Username, String Password){
        this.username = Username;
        this.password = Password;
    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }

}
