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

public class StockScrollAdapter extends RecyclerView.Adapter<StockScrollAdapter.ScrollViewHolder> {

    Context context;
    ArrayList<StockScrollCard> scrollCardList;

    public StockScrollAdapter(Context context, ArrayList<StockScrollCard> scrollCardList) {
        this.context = context;
        this.scrollCardList = scrollCardList;
    }

    @NonNull
    @Override
    public StockScrollAdapter.ScrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScrollViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_scroll_view, parent, false));
    }

    public class ScrollViewHolder extends RecyclerView.ViewHolder {
        TextView stockPrice, stockNum, stockName, stockSymbol, stockID;
        CardView card;
        Button sellStock, buyStock;

        public ScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.stockCard1);
            stockName = itemView.findViewById(R.id.stock1_name);
            stockNum = itemView.findViewById(R.id.stock1_num);
            stockPrice = itemView.findViewById(R.id.stock1_price);
            stockID = itemView.findViewById(R.id.stock1_id);
            stockSymbol = itemView.findViewById(R.id.stock1_symbol);
            sellStock = itemView.findViewById(R.id.sellStock_btn);
            buyStock = itemView.findViewById(R.id.buyStock_btn);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollViewHolder holder, int position) {
        StockScrollCard model = scrollCardList.get(position);
        holder.stockName.setText("" + model.getStockName());
        holder.stockNum.setText("x" + model.getNumPurchased());
        holder.stockPrice.setText("$" + model.getStockPrice());
        holder.stockSymbol.setText("" + model.getSymbol());
        holder.stockID.setText("ID: " + model.getId());

        User Global = User.getInstance();
        //Set ID to 1 if its not instantiated yet
        if (Global.getId() <= 0) Global.setId(1);
        int modelID = model.getId();

        if (model.getNumPurchased() == -1) {
            holder.stockNum.setVisibility(View.INVISIBLE);

            //buttons
            holder.sellStock.setOnClickListener(v -> {
                int numpur = model.getNumPurchased() - 1;
                holder.stockNum.setText("x" + numpur);
                SellStock(this.context, Global, modelID);
            });

            holder.buyStock.setOnClickListener(v -> {
                holder.stockNum.setText("" + model.getNumPurchased() + 1);
                BuyStock(this.context, Global, modelID);
            });
        }
    }

        public void SellStock(Context context, User user,int modelID){
            String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/sell/" + modelID + "/user/" + user.getId() + "/1";

            //Create new request
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    URL_JSON_OBJECT,
                    null,
                    response -> {
                        Log.i("Sell Stock response", "response: " + response);
                        //update user money
                    },
                    error -> Log.i("Sell Stock error", "error: " + error)) {
            };

            // Adding request to request queue
            VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
        }

        public void BuyStock(Context context, User user,int modelID){
            String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/buy/" + modelID + "/user/" + user.getId() + "amt/1";

            //Create new request
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    URL_JSON_OBJECT,
                    null,
                    response -> {
                        Log.i("Buy Stock response", "response: " + response);
                        //update user money
                    },
                    error -> Log.i("Buy Stock error", "error: " + error)) {
            };

            // Adding request to request queue
            VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
        }

        @Override
        public int getItemCount() {
            return scrollCardList.size();
        }


    }

