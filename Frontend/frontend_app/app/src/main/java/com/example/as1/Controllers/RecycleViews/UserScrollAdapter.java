package com.example.as1.Controllers.RecycleViews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.as1.Controllers.LoginAttempt;
import com.example.as1.Controllers.User;
import com.example.as1.ExternalControllers.VolleySingleton;
import com.example.as1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserScrollAdapter extends RecyclerView.Adapter<UserScrollAdapter.ScrollViewHolder> {

    Context context;
    ArrayList<UserScrollCard> scrollCardList;

    public UserScrollAdapter(Context context, ArrayList<UserScrollCard> scrollCardList){
        this.context=context;
        this.scrollCardList = scrollCardList;
    }

    @NonNull
    @Override
    public ScrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScrollViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_user_scroll,parent, false));
    }

    public class ScrollViewHolder extends RecyclerView.ViewHolder {

        TextView usernameV, idV, permV;
        CardView card;
        Button ban,unban;

        public ScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.userCard1);
            usernameV = itemView.findViewById(R.id.user1_name);
            idV = itemView.findViewById(R.id.user1_id);
            permV = itemView.findViewById(R.id.user1_perms);
            ban = itemView.findViewById(R.id.banUser_Adminbtn);
            unban = itemView.findViewById(R.id.unbanUser_Adminbtn);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollViewHolder holder, int position) {
        UserScrollCard model = scrollCardList.get(position);
        holder.usernameV.setText("" + model.getUsername());
        holder.idV.setText("ID: " + model.getId());
        holder.permV.setText("Permission: " + model.getPermission());

        User Global = User.getInstance();
        //Set ID to 1 if its not instantiated yet
        if(Global.getId() <= 0)        Global.setId(1);
        int modelID = (int) model.getId();

        //Ban and Unban buttons
        holder.ban.setOnClickListener(v -> {
            holder.permV.setText("Permission: " + "b");
            postBan(context.getApplicationContext(), Global, modelID);
        });

        holder.unban.setOnClickListener(v -> {
            holder.permV.setText("Permission: " + "u");
            postUnban(context.getApplicationContext(), Global, modelID);
        });
    }

    @Override
    public int getItemCount() {
        return scrollCardList.size();
    }

    public void postBan(Context context, User user, int modelID) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/banuser/" + modelID + "/byadmin/" + user.getId();

        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i("Ban user response", "response: " + response);
                },
                error -> Log.i("Ban user error", "error: " + error)) { };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

    public void postUnban(Context context, User user, int modelID) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/unban/" + modelID + "/byadmin/" + user.getId();

        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i("Unban user response", "response: " + response);
                },
                error -> Log.i("Unban user error", "error: " + error)) { };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }
}