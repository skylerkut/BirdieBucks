package com.example.as1.Controllers.RecycleViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.as1.R;

import java.util.ArrayList;
public class GroupMemScrollAdapter extends RecyclerView.Adapter<GroupMemScrollAdapter.ScrollViewHolder> {

    Context context;
    ArrayList<GroupMemScrollCard> scrollCardList;

    public GroupMemScrollAdapter(Context context, ArrayList<GroupMemScrollCard> scrollCardList){
        this.context=context;
        this.scrollCardList = scrollCardList;
    }

    @NonNull
    @Override
    public ScrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScrollViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_members_scroll,parent, false));
    }

    public class ScrollViewHolder extends RecyclerView.ViewHolder {

        TextView memName,memUsername, memPerm, memID, memMoney, memNumStocks;
        CardView card;

        public ScrollViewHolder(@NonNull View itemView) {
            super(itemView);
              card = itemView.findViewById(R.id.memCard1);
              memID = itemView.findViewById(R.id.mem_id);
              memPerm = itemView.findViewById(R.id.mem_perms);
              memName = itemView.findViewById(R.id.mem_name);
              memUsername = itemView.findViewById(R.id.mem_username);
              memMoney = itemView.findViewById(R.id.mem_money);
              memNumStocks = itemView.findViewById(R.id.mem_numStocks);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollViewHolder holder, int position) {
        GroupMemScrollCard model = scrollCardList.get(position);
        holder.memUsername.setText("" + model.getUsername());
        holder.memName.setText("" + model.getName());
        holder.memID.setText("ID: " + model.getId());
        holder.memPerm.setText("Permission: " + model.getPermission());
        holder.memMoney.setText("" + model.getMoney());
        holder.memNumStocks.setText("" + model.getNumStocks());

        if(model.getPermission() == 'i'){
            holder.memPerm.setVisibility(View.INVISIBLE);
        }
        if(model.getUsername().equals("invs")){
            holder.memUsername.setVisibility(View.INVISIBLE);
        }
        if(model.getMoney() == -2){
            holder.memUsername.setText("# Users: ");
        }

    }

    @Override
    public int getItemCount() {
        return scrollCardList.size();
    }
}
