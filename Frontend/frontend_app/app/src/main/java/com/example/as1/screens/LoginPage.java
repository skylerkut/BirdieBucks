package com.example.as1.screens;

import static com.example.as1.Controllers.User.getInstance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.as1.Controllers.LoginAttempt;
import com.example.as1.Controllers.User;
import com.example.as1.R;
import com.example.as1.ExternalControllers.VolleySingleton;

import android.util.Log;

import org.json.*;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        //Initialize stuff
        Button toHome_btn = findViewById(R.id.toHome_loginPagebtn);
        Button toSignUp_btn = findViewById(R.id.toSignupBtn);
        Button sendLoginReq_btn = findViewById(R.id.sendLoginReq_btn);

        EditText usernameInput_txt = findViewById(R.id.usernameLogin_txtInput);
        EditText passwordInput_txt = findViewById(R.id.passwordLogin_txtInput);
        EditText volleyOutput_txt = findViewById(R.id.loginReqResponse_txt);

        //button back to home page
        toHome_btn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPage.this, StartPage.class);
            startActivity(intent);
        });

        //button to Create Account page
        toSignUp_btn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPage.this, CreateAccountPage.class);
            startActivity(intent);
        });

        //button to try login
        sendLoginReq_btn.setOnClickListener(view -> {
            //parse inputs
            String usernameInput = usernameInput_txt.getText().toString();
            String passwordInput = passwordInput_txt.getText().toString();
            //make new login
           LoginAttempt loginAuth = new LoginAttempt (usernameInput, passwordInput);
            //Post login
           makeLoginPostReq(this.getApplicationContext(), loginAuth);
            Log.i("after post req called", "");

           if(volleyOutput_txt.getText().toString().equals("Success!")) {
               //set global user variables for username and passw
               getInstance().setUsername(usernameInput);
               getInstance().setPassword(passwordInput);
               getUserData(this.getApplicationContext(), usernameInput);
               //go to MainPage
               Intent intent = new Intent(LoginPage.this, NavPage.class);
               startActivity(intent);
           }
        });
    }

    public void makeLoginPostReq(Context context, LoginAttempt loginA) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/login";
                //"http://coms-309-051.class.las.iastate.edu:8080/login";
        //http://10.90.75.130:8080/login
        EditText volleyOutput_txt = findViewById(R.id.loginReqResponse_txt);
        JSONObject objectBody = new JSONObject();

        // Convert input to JSONObject
        try {
            objectBody.put("username",loginA.getUsername());
            objectBody.put("password",loginA.getPassword());
            Log.i("loginA JSON Object: ", objectBody.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL_JSON_OBJECT,
                objectBody,
                response -> {
                    try {
                        volleyOutput_txt.setText(response.getString("success"));
                    } catch (JSONException e) {
                        Log.i("response.getString", "Error message: ");
                        throw new RuntimeException(e);
                    }
                    Log.i("Post req response", "makeLoginPostReq: " + response);
                },
                error -> Log.i("Login error", "error message: " + error)) { };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

    public void getUserData(Context context, String username) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/userByName/" + username;

        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null,
                response -> {
                    try {
                        // Parse JSON object data
                        String name = response.getString("name");
                        String email = response.getString("email");
                        String id = response.getString("id");
                        String dob = response.getString("dob");
                        String money = response.getString("money");

                        // Populate global user with the parsed data
                        getInstance().setName(name);
                        getInstance().setEmail(email);
                        getInstance().setId(Integer.parseInt(id));
                        getInstance().setDob(dob);
                        getInstance().setMoney(Double.parseDouble(money));
                        Log.i("Login data response", "getUserData: " + getInstance());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.i("error msg", "getUserData: " + error.getMessage())) {
        };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

}