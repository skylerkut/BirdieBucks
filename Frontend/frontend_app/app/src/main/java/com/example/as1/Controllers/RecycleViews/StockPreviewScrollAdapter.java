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

public class StockPreviewScrollAdapter extends RecyclerView.Adapter<StockPreviewScrollAdapter.ScrollViewHolder> {

    Context context;
    ArrayList<StockPreviewScrollCard> scrollCardList;

    public StockPreviewScrollAdapter(Context context, ArrayList<StockPreviewScrollCard> scrollCardList){
        this.context=context;
        this.scrollCardList = scrollCardList;
    }

    @NonNull
    @Override
    public ScrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScrollViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_preview_scroll,parent, false));
    }

    public class ScrollViewHolder extends RecyclerView.ViewHolder {

        TextView company, idV, val;
        CardView card;
        Button delete;

        public ScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.stockPCard1);
            company = itemView.findViewById(R.id.stockP_company);
            idV = itemView.findViewById(R.id.stockP_id);
            val = itemView.findViewById(R.id.stockP_currValue);
            delete = itemView.findViewById(R.id.deleteStock_Adminbtn);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollViewHolder holder, int position) {
        StockPreviewScrollCard model = scrollCardList.get(position);
        holder.company.setText("" + model.getCompany());
        holder.idV.setText("ID: " + model.getId());
        holder.val.setText("$" + model.getCurrValue());

        User Global = User.getInstance();
        //Set ID to 1 if its not instantiated yet
        if(Global.getId() <= 0) Global.setId(1);
        int modelID = (int) model.getId();

        //Delete stock button
        holder.delete.setOnClickListener(v -> {
            holder.company.setText("Deleted Stock");
            holder.idV.setText("--");
            holder.val.setText("--");
            holder.delete.setVisibility(View.INVISIBLE);
            deleteStock(context.getApplicationContext(), Global, modelID);
        });

    }

    @Override
    public int getItemCount() {
        return scrollCardList.size();
    }

    public void deleteStock(Context context, User user, int modelID) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/stocks/" + modelID + "/" + user.getId();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i("Delete stock response", "response: " + response);
                },
                error -> Log.i("Delete stock error", "error: " + error)) { };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

}