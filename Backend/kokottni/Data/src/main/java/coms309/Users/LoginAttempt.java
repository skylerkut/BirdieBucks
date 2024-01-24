package coms309.Users;

public class LoginAttempt{
    String username;
    String password;

    String success;

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

    public String getSuccess(){return this.success;}

    public void setSuccess(String Success){this.success = Success;}
}

