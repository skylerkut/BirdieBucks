package com.example.as1.Controllers.RecycleViews;

import android.content.Context;
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

import java.util.ArrayList;

public class GroupLeaderAdapter extends RecyclerView.Adapter<GroupLeaderAdapter.ScrollViewHolder> {

    Context context;
    ArrayList<GroupLeaderCard> scrollCardList;

    public GroupLeaderAdapter(Context context, ArrayList<GroupLeaderCard> scrollCardList) {
        this.context = context;
        this.scrollCardList = scrollCardList;
    }

    @NonNull
    @Override
    public GroupLeaderAdapter.ScrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupLeaderAdapter.ScrollViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_leader_scroll, parent, false));
    }

    public class ScrollViewHolder extends RecyclerView.ViewHolder {

        TextView memName, memUsername, memPerm, memID;
        CardView card;
        Button remove_btn, setLeader_btn;

        public ScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.groupMem_leaderCard);
            memID = itemView.findViewById(R.id.Lmem_id);
            memPerm = itemView.findViewById(R.id.Lmem_perms);
            memName = itemView.findViewById(R.id.Lmem_name);
            memUsername = itemView.findViewById(R.id.Lmem_username);
            remove_btn = itemView.findViewById(R.id.remove_groupMem_btn);
            setLeader_btn = itemView.findViewById(R.id.setLeader_btn);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GroupLeaderAdapter.ScrollViewHolder holder, int position) {
        GroupLeaderCard model = scrollCardList.get(position);
        holder.memUsername.setText("" + model.getUsername());
        holder.memName.setText("" + model.getName());
        holder.memID.setText("ID: " + model.getId());
        holder.memPerm.setText("Permission: " + model.getPermission());

        User global = User.getInstance();
        String groupName;
        if(global.getFriendGroup() == null){
            groupName = "name";
        } else {
            groupName = global.getFriendGroup().getGroupName();
        }
        long Gid = global.getId();;
        if (global.getId() < 0){
            Gid = 2;
        }

        long finalGid = Gid;
        holder.remove_btn.setOnClickListener(v -> {
            removeUser(this.context, groupName, finalGid, model.getId(), position);
            holder.memUsername.setVisibility(View.INVISIBLE);
            holder.memName.setText("User Removed");
            holder.memID.setVisibility(View.INVISIBLE);
            holder.memPerm.setVisibility(View.INVISIBLE);
            holder.setLeader_btn.setVisibility(View.INVISIBLE);
            holder.remove_btn.setVisibility(View.INVISIBLE);
        });

        holder.setLeader_btn.setOnClickListener(v -> {
            setNewLeader(this.context,  groupName, finalGid, model.getId(), position);
            holder.memPerm.setText("Permission: 'g'");
            holder.setLeader_btn.setVisibility(View.INVISIBLE);
            holder.remove_btn.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return scrollCardList.size();
    }

    public void removeUser(Context context, String groupName, long leaderID, long userID, int position) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/friendgroup/remove/" + groupName + "/" + leaderID + "/" + userID;

        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i("Remove user response", "response: " + response);
                },
                error -> Log.i("Remove user error", "error: " + error)) {};

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

    public void setNewLeader(Context context, String groupName, long leaderID, long userID, int position) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/friendgroup/setnewleader/" + groupName + "/" + leaderID + "/" + userID;

        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i("Set Leader", "response: " + response);
                    },
                error -> Log.i("Set leader", "error: " + error)) {};

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }
}
