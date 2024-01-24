package com.example.as1.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.as1.Controllers.RecycleViews.GroupLeaderAdapter;
import com.example.as1.Controllers.RecycleViews.GroupLeaderCard;
import com.example.as1.Controllers.RecycleViews.GroupMemScrollCard;
import com.example.as1.Controllers.RecycleViews.GroupMemScrollAdapter;
import com.example.as1.Controllers.StockPurchased;
import com.example.as1.ExternalControllers.VolleySingleton;
import com.example.as1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupAdminPage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_admin_page);

        //Back
        Button back = findViewById(R.id.back_groupAdminBtn);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(GroupAdminPage.this, GroupPage.class);
            startActivity(intent);
        });

        //Initialize recycler view (user)
        RecyclerView recyclerView = findViewById(R.id.groupUser_scroll);
        ArrayList<GroupLeaderCard> GroupCardArray = new ArrayList<>();
        GroupCardArray.add(new GroupLeaderCard("noname", "noInputs", 0,'u'));
        GroupLeaderAdapter groupLeaderAdapter = new GroupLeaderAdapter(this, GroupCardArray);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //Set recycler view
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(groupLeaderAdapter);
        getGroupMembers(this.getApplicationContext(), "name");

    }
    public void getGroupMembers(Context context, String groupName) {
        String URL_JSON_OBJECT1 = "http://coms-309-051.class.las.iastate.edu:8080/friendgroup/getall/" + groupName;

        ArrayList<GroupLeaderCard> GroupCardArrayList= new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.groupUser_scroll);

        //Create new request
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_JSON_OBJECT1,
                null,
                response -> {
                    Log.i(" full response", "getAllGroupMembers: " + response.toString());

                    JSONObject object;

                    for(int i = 0; i < response.length(); i++) {
                        try {
                            object = (JSONObject) response.get(i);

                            //parse relevant info
                            String username = object.getString("username");
                            String name = object.getString("name");
                            int id = object.getInt("id");
                            char priv = object.getString("privilege").toCharArray()[0];

                            GroupCardArrayList.add(new GroupLeaderCard(name,username, id, priv));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    //Initialize recycler view
                    GroupLeaderAdapter groupLeaderAdapter = new GroupLeaderAdapter(this, GroupCardArrayList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

                    //Set recycler view
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(groupLeaderAdapter);
                },

                error -> Log.i("Error ", "getAllGroupMembers: "+ error.getMessage())) {};

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

}