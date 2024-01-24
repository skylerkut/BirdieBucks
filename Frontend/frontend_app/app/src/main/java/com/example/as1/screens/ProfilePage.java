package com.example.as1.screens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.as1.Controllers.RecycleViews.StockScrollAdapter;
import com.example.as1.Controllers.RecycleViews.StockScrollCard;
import com.example.as1.Controllers.Stock;
import com.example.as1.Controllers.StockPurchased;
import com.example.as1.R;
import com.example.as1.Controllers.User;
import com.example.as1.ExternalControllers.VolleySingleton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Menu menu;
    ActionBarDrawerToggle toggle;
    private static final String URL_IMAGE ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        Button editProfile_btn = findViewById(R.id.editProfile_btn);
        Button toStock_btn = findViewById(R.id.toStockPage_btn);
        TextView welcomeTxt = findViewById(R.id.welcome_txtView);
        TextView username_display = findViewById(R.id.username_Display);
        TextView password_display = findViewById(R.id.password_Display);
        TextView email_display = findViewById(R.id.email_Display);
        TextView dob_display = findViewById(R.id.dob_Display);
        TextView money_display = findViewById(R.id.money_Display);
        ShapeableImageView defaultPic = findViewById(R.id.picSample);

        //Side nav bar
        drawerLayout = findViewById(R.id.drawer_layout_profile);
        navigationView = findViewById(R.id.nav_view_profile);
        menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);
        menu.findItem(R.id.nav_profile).setVisible(false);
        menu.findItem(R.id.nav_group).setVisible(false);
        menu.findItem(R.id.nav_group_edit).setVisible(false);

        navigationView.bringToFront();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Get global user data for get request (just need id for get req)
        User getGlobal = User.getInstance();
        //Get req for user data, need to be sure global user has id set after logging in
        getGlobal = getUserData(this.getApplicationContext(),getGlobal);

        //Set numStocks card
        getNumStocks(this.getApplicationContext(), getGlobal);

        //set display to user data
        welcomeTxt.setText("Welcome, " + getGlobal.getName() + "!");
        username_display.setText(getGlobal.getUsername());
        password_display.setText(getGlobal.getPassword());
        email_display.setText(getGlobal.getEmail());
        dob_display.setText(getGlobal.getDob());
        double money1 = getGlobal.getMoney();
        money_display.setText("$" + String.valueOf(money1));


//        //Back to Home page button
//        backHome_btn.setOnClickListener(view -> {
//            Intent intent = new Intent(ProfilePage.this, NavPage.class);
//            startActivity(intent);
//        });

        //Edit Profile button
        editProfile_btn.setOnClickListener(view -> {
            Intent intent = new Intent(ProfilePage.this, EditProfilePage.class);
            startActivity(intent);
        });

        //To Stock Page button
        toStock_btn.setOnClickListener(view -> {
            Intent intent = new Intent(ProfilePage.this, PortfolioPage.class);
            startActivity(intent);
        });

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
                        user.setUsername(username);
                        user.setPassword(password);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.i("error msg", "getUserData: " + error.getMessage())) {
        };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
        return user;
    }

    public void getNumStocks(Context context, User user) {
        String URL_JSON_OBJECT = "http://10.90.75.130:8080/user/" + user.getId();
        TextView stock_display = findViewById(R.id.stock_Display);

        //Create new request
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i(" full response", "getNumStocks: " + response.length());
                    stock_display.setText("#" + response.length());
                },

                error -> Log.i("error ", "getNumStocks: "+ error.getMessage())) {};

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }



    //TODO:Image get and post to set profile picture
    /**
     * Making image GET request
     * */
    private void ImageGetRequest() {

        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                (Response.Listener<Bitmap>) response -> {
                    //on response
                },
                0, // Width, set to 0 to get the original width
                        0, // Height, set to 0 to get the original height
                        ImageView.ScaleType.FIT_XY, // ScaleType
                        Bitmap.Config.RGB_565, // Bitmap config

                (Response.ErrorListener) error -> {
                    // Handle errors here
                    Log.d("Volley Error", error.toString());
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }

    /**
     * Making image POST request
     * */
    private void ImagePostRequest() {

        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                (Response.Listener<Bitmap>) response -> {
                    //on response
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                (Response.ErrorListener) error -> {
                    // Handle errors here
                    Log.d("Volley Error", error.toString());
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }

    /*
     Nav Bar Functions
  */
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home){
            Intent intent = new Intent(ProfilePage.this, NavPage.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.nav_stock){
            Intent intent = new Intent(ProfilePage.this, StockPage.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.nav_stock_list) {
            Intent intent = new Intent(ProfilePage.this, StockList.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.nav_login) {
            Intent intent = new Intent(ProfilePage.this, StartPage.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

}