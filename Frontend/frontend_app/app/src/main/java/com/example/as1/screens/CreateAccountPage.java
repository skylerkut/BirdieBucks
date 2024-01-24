package com.example.as1.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.as1.R;
import com.example.as1.Controllers.User;
import com.example.as1.ExternalControllers.VolleySingleton;

import org.json.JSONObject;

public class CreateAccountPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        Button toHome_btn;
        Button toLogin_btn;
        Button sendSignupReq_btn;
        EditText usernameInput_txt = findViewById(R.id.usernameSignup_txtInput);
        EditText passwordInput_txt = findViewById(R.id.passwordSignup_txtInput);
        EditText volleyOutput_txt = findViewById(R.id.signupReqResponse_txt);

        //button back to home page
        toHome_btn = findViewById(R.id.toHome_SignupPagebtn);
        toHome_btn.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccountPage.this, StartPage.class);
            startActivity(intent);
        });

        //button to Login page
        toLogin_btn = findViewById(R.id.toLogin_frmSignUp_Btn);
        toLogin_btn.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccountPage.this, LoginPage.class);
            startActivity(intent);
        });

        //button to try login
        sendSignupReq_btn = findViewById(R.id.sendSignupReq_btn);
        sendSignupReq_btn.setOnClickListener(view -> {

            //make new user


            //parse inputs
            String usernameInput = usernameInput_txt.getText().toString();
            String passwordInput = passwordInput_txt.getText().toString();
            User signupUser = new User();
            //= new User (usernameInput, passwordInput);
            //have backend make signup class like login class

            //Post login
            makeSignUPPostReq(this.getApplicationContext(), signupUser);

            //If backend returns success, open main page
            if(volleyOutput_txt.getText().toString() == "{\"message\":\"success\"}"){
                Intent intent = new Intent(CreateAccountPage.this, NavPage.class);
                startActivity(intent);
            }
        });
    }

    public void makeSignUPPostReq(Context context, User userSignup) {
        String URL_JSON_OBJECT = "http://10.90.75.130:8080/users";
        EditText volleyOutput_txt = findViewById(R.id.signupReqResponse_txt);

        // Convert input to JSONObject
        JSONObject objectBody = new JSONObject();
        try {
            objectBody = new JSONObject(
                    "{username : " + userSignup.getUsername().toString() + "}" +
                    "{password : " + userSignup.getPassword().toString() + "}");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL_JSON_OBJECT,
                objectBody,
                //response function to lambda
                response -> volleyOutput_txt.setText(response.toString()),
                error -> Log.e("Volley Error", volleyOutput_txt.toString())
        //volleyOutput_txt.setText(error.getMessage())
               //
        ){ };
        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }



}