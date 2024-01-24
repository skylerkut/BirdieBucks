package com.example.as1.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.as1.Controllers.RecycleViews.GroupAdapter;
import com.example.as1.Controllers.RecycleViews.GroupMemScrollCard;
import com.example.as1.Controllers.RecycleViews.GroupMemScrollAdapter;
import com.example.as1.Controllers.RecycleViews.GroupScrollCard;
import com.example.as1.Controllers.StockPurchased;
import com.example.as1.Controllers.User;
import com.example.as1.ExternalControllers.VolleySingleton;
import com.example.as1.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Menu menu;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_page);

        //Side nav bar
        drawerLayout = findViewById(R.id.drawer_layout_group);
        navigationView = findViewById(R.id.nav_view_group);
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


        //to friends chat button
        Button toChat_btn = findViewById(R.id.toChat_friendGroupBtn);
        toChat_btn.setOnClickListener(view -> {
            Intent intent = new Intent(GroupPage.this, GroupChatPage.class);
            startActivity(intent);
        });

        //to friends chat button
        Button toAdmin_btn = findViewById(R.id.toAdmin_GroupBtn);
        toAdmin_btn.setOnClickListener(view -> {
            Intent intent = new Intent(GroupPage.this, GroupAdminPage.class);
            startActivity(intent);
        });

        //Create new Group
        Button createGroup = findViewById(R.id.createGroup_Adminbtn);
        createGroup.setOnClickListener(view -> {
            Intent intent = new Intent(GroupPage.this, CreateGroup.class);
            startActivity(intent);
        });

        //Initialize recycler view (user)
        RecyclerView recyclerView = findViewById(R.id.yourGroup_recyc);
        ArrayList<GroupMemScrollCard> GroupMemCardArrayList = new ArrayList<>();
        GroupMemCardArrayList.add(new GroupMemScrollCard("NoInputs", "NoInputs", 0, 'u', 0, 0));
        GroupMemScrollAdapter groupMemScrollAdapter = new GroupMemScrollAdapter(this, GroupMemCardArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Set recycler view
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(groupMemScrollAdapter);
        getGroupMembers(this.getApplicationContext(), "StockGroup1");


        //Initialize recycler view (groups)
        RecyclerView recyclerView2 = findViewById(R.id.otherGroups_recyc);
        ArrayList<GroupScrollCard> GroupCardArrayList = new ArrayList<>();
        GroupCardArrayList.add(new GroupScrollCard("NoInputs", 0));
        GroupAdapter groupAdapter = new GroupAdapter(this, GroupCardArrayList);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Set recycler view
        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView2.setAdapter(groupAdapter);
        getAllGroups(this.getApplicationContext());

    }//onCreate

    public void getGroupMembers(Context context, String groupName) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/friendgroup/getall/" + groupName;

        ArrayList<GroupMemScrollCard> GroupMemCardArrayList= new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.yourGroup_recyc);

        //Create new request
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i(" full response", "getAllGroupMembers: " + response.toString());

                    JSONObject object;

                    for(int i = 0; i < response.length(); i++) {
                        try {
                            object = (JSONObject) response.get(i);
                            // JSONObject stockObj = (JSONObject) object.get("user");
                            //User userIN = new User();

                            //parse relevant info
                            String username = object.getString("username");
                            String name = object.getString("name");
                            int id = object.getInt("id");
                            char priv = object.getString("privilege").toCharArray()[0];
                            int money = object.getInt("money");

//                            List<StockPurchased> stocks = new ArrayList<>();
//                            stocks = (List<StockPurchased>) object.get("stocks");
                            int numStocks = 1;
                            //add to arraylist to be displayed in recycle view
                            GroupMemCardArrayList.add(new GroupMemScrollCard(name,username, id, priv, money, numStocks));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    //Initialize recycler view
                    GroupMemScrollAdapter groupMemScrollAdapter = new GroupMemScrollAdapter(this, GroupMemCardArrayList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

                    //Set recycler view
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(groupMemScrollAdapter);
                },

                error -> Log.i("Error ", "getAllGroupMembers: "+ error.getMessage())) {};

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

    public void getAllGroups(Context context) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/friendgroup";

        ArrayList<GroupScrollCard> GroupCardArrayList= new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.otherGroups_recyc);

        //Create new request
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i(" full response", "getAllGroups: " + response.toString());

                    JSONObject object;

                    for(int i = 0; i < response.length(); i++) {
                        try {
                            object = (JSONObject) response.get(i);

                            //parse relevant info
                            String groupName = object.getString("groupName");
                            int leaderID = object.getInt("groupLeader");
                            GroupCardArrayList.add(new GroupScrollCard(groupName, leaderID));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //Initialize recycler view
                    GroupAdapter groupAdapter = new GroupAdapter(this, GroupCardArrayList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

                    //Set recycler view
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(groupAdapter);
                },

                error -> Log.i("Error ", "getAllGroups: "+ error.getMessage())) {};

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
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
            Intent intent = new Intent(GroupPage.this, NavPage.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.nav_stock){
            Intent intent = new Intent(GroupPage.this, StockPage.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.nav_stock_list) {
            Intent intent = new Intent(GroupPage.this, StockList.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.nav_login) {
            Intent intent = new Intent(GroupPage.this, StartPage.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}//end
