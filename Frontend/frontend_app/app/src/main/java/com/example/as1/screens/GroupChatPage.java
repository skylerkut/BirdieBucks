package com.example.as1.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.as1.Controllers.FriendGroup;
import com.example.as1.Controllers.User;
import com.example.as1.ExternalControllers.VolleySingleton;
import com.example.as1.ExternalControllers.WebSocketListener;
import com.example.as1.ExternalControllers.WebSocketManager;
import com.example.as1.R;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupChatPage extends AppCompatActivity implements WebSocketListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_chat_page);

         String BASE_URL = "ws://coms-309-051.class.las.iastate.edu:8080/chat/";
         EditText usernameEtx;
         TextView msgTv;

        Button sendBtn =  findViewById(R.id.bt2);
        EditText msgEtx =  findViewById(R.id.et2);
        TextView welcome = findViewById(R.id.welcome_display);

        //TODO: get call /chat/messages/target for messages history

        //Get global user data for get request (just need id for get req)
        User getGlobal = User.getInstance();
        getFriendGroup(this.getApplicationContext(),getGlobal);

        welcome.setText("Welcome " + getGlobal.getName() + "!");

            String groupURL;
        if(getGlobal.getFriendGroup() != null){
            groupURL = getGlobal.getFriendGroup().getGroupName();
        } else {
            groupURL = "StockGroup1";
        }
        String serverUrl = BASE_URL + getGlobal.getName().toString() + "/" + groupURL;

            // Establish WebSocket connection and set listener
            WebSocketManager.getInstance().connectWebSocket(serverUrl);
            WebSocketManager.getInstance().setWebSocketListener(GroupChatPage.this);

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            try {
                // send message
                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
            } catch (Exception e) {
                Log.i("ExceptionSendMessage:", e.getMessage().toString());
            }
        });

        //back to main button
        Button backHome_btn = findViewById(R.id.backHome_FriendsBtn);
        backHome_btn.setOnClickListener(view -> {
            Intent intent = new Intent(GroupChatPage.this, GroupPage.class);
            startActivity(intent);
        });


    }

    //Websocket Functions
    @Override
    public void onWebSocketMessage(String message) {
        TextView msgTv =  findViewById(R.id.tx1);
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n"+message);
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        TextView msgTv =  findViewById(R.id.tx1);
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}
    @Override
    public void onWebSocketError(Exception ex) {}

    //JSON Request
    public FriendGroup getFriendGroup(Context context, User user) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/friendgroup/get/" + user.getId();
        FriendGroup friendGroup = new FriendGroup();
        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null,
                response -> {
                    try {

                        if(response.get("friendGroup") == null){
                            return;
                        }
                        JSONObject group;
                        group = (JSONObject) response.get("friendGroup");

                        friendGroup.setGroupName((group.getString("groupName")));
                        friendGroup.setGroupLeaderID(group.getLong("groupLeaderID"));

                        JSONArray groupMembers = response.getJSONArray("groupMembers");
                        User holder = new User();
                        List<User> groupUsers = new ArrayList<>();
                        for(int i = 0; i < groupMembers.length(); i++){
                            holder = (User)groupMembers.get(i);
                            groupUsers.set(i, holder);
                        }
                        friendGroup.setGroupMembers(groupUsers);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.i("error msg", "getUserData: " + error.getMessage())) {
        };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
        return friendGroup;
    }
}
