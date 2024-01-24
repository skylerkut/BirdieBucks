package com.example.as1.Controllers.RecycleViews;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.as1.Controllers.User;
import com.example.as1.ExternalControllers.VolleySingleton;
import com.example.as1.R;
import com.example.as1.screens.GroupChatPage;
import com.example.as1.screens.GroupPage;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ScrollViewHolder> {

    Context context;
    ArrayList<GroupScrollCard> scrollCardList;

    public GroupAdapter(Context context, ArrayList<GroupScrollCard> scrollCardList){
        this.context=context;
        this.scrollCardList = scrollCardList;
    }

    @NonNull
    @Override
    public GroupAdapter.ScrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupAdapter.ScrollViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_scroll,parent, false));
    }

    public class ScrollViewHolder extends RecyclerView.ViewHolder {

        TextView groupName, numMembers_txt;
        CardView card;
        Button join;

        public ScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.groupsCard);
            groupName = itemView.findViewById(R.id.group_name);
            numMembers_txt = itemView.findViewById(R.id.num_groupMembers);
            join = itemView.findViewById(R.id.join_group_btn);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.ScrollViewHolder holder, int position) {
        GroupScrollCard model = scrollCardList.get(position);
        holder.groupName.setText("" + model.getGroupName());
        holder.numMembers_txt.setText("Leader ID: " + model.getNumMembers());

        User Global = User.getInstance();
        //Set ID to 1 if its not instantiated yet
        if(Global.getId() <= 0)        Global.setId(1);

        //Ban and Unban buttons
        holder.join.setOnClickListener(v -> {
            joinGroup(context.getApplicationContext(), Global.getId(), model.groupName);
        });

    }

    @Override
    public int getItemCount() {
        return scrollCardList.size();
    }

    public void joinGroup(Context context, long userID, String groupName) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/friendgroup/" + groupName + "/" + userID;

        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i("Join Group", "response: " + response);
                },
                error -> Log.i("Join Group", "error: " + error)) { };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

}
