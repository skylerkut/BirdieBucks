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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;

public class EditProfilePage extends AppCompatActivity {

//TODO: need universal user ID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        //Instantiate Buttons
        Button back_btn = findViewById(R.id.EbackProfile_btn);
        Button saveProfile_btn = findViewById(R.id.saveProfile_btn);
//        Button toPortfolio_btn = findViewById(R.id.toStockPage_btn);
        EditText welcomeTxt = findViewById(R.id.Ewelcome_txtView);
        EditText username_display = findViewById(R.id.Eusername_Display);
        EditText password_display = findViewById(R.id.Epassword_Display);
        EditText email_display = findViewById(R.id.Eemail_Display);
        EditText dob_display = findViewById(R.id.Edob_Display);
        EditText money_display = findViewById(R.id.Emoney_Display);

        //Get global user data for get request
        AtomicReference<User> getGlobal = new AtomicReference<>(User.getInstance());
        //Get req for user data, need to be sure global user has id set after logging in
        //getGlobal = getUserData(this.getApplicationContext(),getGlobal);

        //set display to user data
        welcomeTxt.setText(getGlobal.get().getName());
        username_display.setText(getGlobal.get().getUsername().toString());
        password_display.setText(getGlobal.get().getPassword().toString());
        email_display.setText(getGlobal.get().getEmail().toString());
        dob_display.setText(getGlobal.get().getDob().toString());
        double money1 = (double) getGlobal.get().getMoney();
        money_display.setText(String.valueOf(money1));

        // save changes button
        saveProfile_btn.setOnClickListener(view -> {
            //write inputs to user
            getGlobal.get().setName(welcomeTxt.getText().toString());
            getGlobal.get().setUsername(username_display.getText().toString());
            getGlobal.get().setPassword(password_display.getText().toString());
            getGlobal.get().setEmail(email_display.getText().toString());
            getGlobal.get().setDob(dob_display.getText().toString());
            getGlobal.get().setMoney(Double.parseDouble(money_display.getText().toString()));
            //post request
            getGlobal.set(updateUserReq(this.getApplicationContext(), getGlobal.get()));
            //go back to profile page
            Intent intent = new Intent(EditProfilePage.this, ProfilePage.class);
            startActivity(intent);

        });


        //back button
        back_btn.setOnClickListener(view -> {
            Intent intent = new Intent(EditProfilePage.this, ProfilePage.class);
            startActivity(intent);
        });

//        //to Portfolio button
//        toPortfolio_btn.setOnClickListener(view -> {
//            Intent intent = new Intent(EditProfilePage.this, PortfolioPage.class);
//            startActivity(intent);
//        });

    }

    public User getUserData(Context context, User user) {

        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/userByName/" + user.getUsername();
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
                        String username = response.getString("username");
                        String password = response.getString("password");

                        // Populate text views with the parsed data
                        user.setName(name);
                        user.setEmail(email);
                        user.setId(Integer.parseInt(id));
                        user.setDob(dob);
                        user.setMoney(Double.parseDouble(money));
                       // user.setNumStocks(Integer.parseInt(numStocks));
                        user.setUsername(username);
                        user.setPassword(password);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error ->  Log.i("error", "getUserData: " + error)) {
        };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
        return user;
    }

    public User updateUserReq(Context context, User user) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/users/" + user.getId();
        JSONObject objectBody = new JSONObject();
        // Convert input to JSONObject
        try {
            objectBody.put("username",user.getUsername());
            objectBody.put("password",user.getPassword());
            objectBody.put("name",user.getName());
            objectBody.put("dob",user.getDob());
            objectBody.put("email",user.getEmail());
            objectBody.put("money",user.getMoney());
            objectBody.put("id",user.getId());

            Log.i("user JSON Object: ", objectBody.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL_JSON_OBJECT,
                objectBody,
                response ->{
                    Log.i("response", "updateUserReq: " + response);
                },
                error -> Log.i("error", "updateUserReq: " + error)) { };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
        return user;
    }

}