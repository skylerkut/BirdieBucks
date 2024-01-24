package com.example.as1.screens;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.as1.Controllers.RecycleViews.StockPreviewScrollAdapter;
import com.example.as1.Controllers.RecycleViews.StockPreviewScrollCard;
import com.example.as1.Controllers.Stock;
import com.example.as1.Controllers.User;
import com.example.as1.ExternalControllers.VolleySingleton;
import com.example.as1.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class StockPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Button newStock_btn, pageLeft, pageRight, buy_btn, sell_btn, delete_btn, toNews_btn;
    protected TextView stockName, stockSymbol, serverNotes;
    protected EditText curr_display;

   protected TextView id_display, stockChange;
   ImageView stockImage, stockLogo;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Menu menu;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_page);
        final int[] index = {0};

        //Set thread permissions to access image url over network
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        //get all stocks in an array
        ArrayList<Stock> stockArrayList = new ArrayList<>();
        stockArrayList = getAllStocks(this.getApplicationContext());
        Log.i("getAllStocks req", "stocklist array: " + stockArrayList.toString());

        //set first image
        getStockLogo(this.getApplicationContext(), 1);

        //Side nav bar
        drawerLayout = findViewById(R.id.drawer_layout_stock);
        navigationView = findViewById(R.id.nav_view_stock);
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

//        //Back Button
//        back_btn = findViewById(R.id.back_StockPageBtn);
//        back_btn.setOnClickListener(view -> {
//            Intent intent = new Intent(StockPage.this, NavPage.class);
//            startActivity(intent);
//        });

        //Create Stock Button
        newStock_btn = findViewById(R.id.createStock_StockPageBtn);
        serverNotes = findViewById(R.id.notesEditText);
        User getGlobal = User.getInstance();
        newStock_btn.setOnClickListener(view -> {
            if(getGlobal.getPrivilege() != 'a'){
                serverNotes.setText("Only Admins can create Stocks");
            }
            else {
                Intent intent = new Intent(StockPage.this, CreateStock.class);
                startActivity(intent);
            }
        });

        //View News Button
        toNews_btn = findViewById(R.id.toNews_btn);
        toNews_btn.setOnClickListener(view -> {
                Intent newsIntent = new Intent(StockPage.this, NewsPage.class);
                //+ 1 so index matches stock id
                index[0] += 1;
                newsIntent.putExtra("index", index[0]);
                startActivity(newsIntent);
        });

//        //Buy Button
//        buy_btn = findViewById(R.id.buy_StockPagebtn);
//        ArrayList<Stock> finalStockArrayList = stockArrayList;
//        buy_btn.setOnClickListener(view -> {
//            Stock object;
//            object = finalStockArrayList.get(index[0]);
//            long id = object.getId();
//            BuyStock(this.getApplicationContext(), getGlobal, (int) id);
//        });
//
//        //Sell Button
//        sell_btn = findViewById(R.id.sell_StockPagebtn);
//        ArrayList<Stock> finalStockArrayList4 = stockArrayList;
//        sell_btn.setOnClickListener(view -> {
//            Stock object;
//            object = finalStockArrayList4.get(index[0]);
//            long id2 = object.getId();
//            SellStock(this.getApplicationContext(), getGlobal, (int) id2);
//        });

        //History Button
        Button toHistory_btn = findViewById(R.id.toHistory_btn);
        toHistory_btn.setOnClickListener(view -> {
            Intent historyIntent = new Intent(StockPage.this, HistoryPage.class);
            //+ 1 so index matches stock id
            index[0] += 1;
            historyIntent.putExtra("index", index[0]);
            startActivity(historyIntent);
        });

//        //Delete Stock Button
//        delete_btn = findViewById(R.id.delete_StockPagebtn);
//        serverNotes = findViewById(R.id.notesEditText);
//        id_display = findViewById(R.id.stockID_view);
//        curr_display  = findViewById(R.id.stockPrice_Display);
//        ArrayList<Stock> finalStockArrayList3 = stockArrayList;
//        newStock_btn.setOnClickListener(view -> {
//            if(getGlobal.getPrivilege() != 'a'){
//                serverNotes.setText("Only Admins can delete Stocks");
//            }
//            else {
//                //delete stock
//                Stock object;
//                object = finalStockArrayList3.get(index[0]);
//                long id3 = object.getId();
//                deleteStock(this.getApplicationContext(), getGlobal, (int) id3);
//                //remove from arraylist
//                finalStockArrayList3.remove(index[0]);
//                index[0] -= 1;
//                //reset view to stock at previous index
//                object = finalStockArrayList3.get(index[0]);
//                stockName.setText(object.getCompany());
//                stockSymbol.setText(object.getSymbol());
//                id_display.setText("ID: " + Math.toIntExact(object.getId()));
//                curr_display.setText("" + (int) object.getCurrValue());
//
//                double stockPercentNum = (object.getPrevDayChange() / object.getCurrValue()) * 100;
//                String stockPercent = String.format("%.3f", stockPercentNum);
//                stockChange.setText("" + object.getPrevDayChange() + " || " + stockPercent + "%");
//                if(object.getPrevDayChange() < 0){
//                    stockImage.setImageResource(R.drawable.stockredarrow);
//                }
//                else{
//                    stockImage.setImageResource(R.drawable.greenarrow);
//                }
//            }
//        });

        //Page Left Button
        pageLeft = findViewById(R.id.prev_StockPageBtn);
        ArrayList<Stock> finalStockArrayList2 = stockArrayList;
        final int[] nextIndex2 = new int[1];
        pageLeft.setOnClickListener(view -> {
            if(finalStockArrayList2 == null){
                serverNotes.setText("Stock List is null, please try again later");
            }
            else {
                int size = finalStockArrayList2.size()-1;
                if(size < 0){
                    size = 0;
                }
                if(index[0] == 0){
                    index[0] = size;
                    nextIndex2[0] = size;
                }
                else {
                    nextIndex2[0] = index[0] - 1;
                    index[0] = nextIndex2[0];
                }
                Stock object;
                object = finalStockArrayList2.get(nextIndex2[0]);
                stockName.setText(object.getCompany());
                stockSymbol.setText(object.getSymbol());
                id_display.setText("ID: " + Math.toIntExact(object.getId()));
                curr_display.setText("" + (int) object.getCurrValue());

                getStockLogo(this.getApplicationContext(), Math.toIntExact(object.getId()));

                double stockPercentNum = (object.getPrevDayChange() / object.getCurrValue()) * 100;
                String stockPercent = String.format("%.3f", stockPercentNum);
                stockChange.setText("" + object.getPrevDayChange() + " || " + stockPercent + "%");
                if(object.getPrevDayChange() < 0){
                    stockImage.setImageResource(R.drawable.stockredarrow);
                }
                else{
                    stockImage.setImageResource(R.drawable.greenarrow);
                }
            }
        });

        //Page Right Button
        pageRight = findViewById(R.id.next_StockPageBtn);
        ArrayList<Stock> finalStockArrayList1 = stockArrayList;
        final int[] nextIndex = new int[1];
        pageRight.setOnClickListener(view -> {
            if(finalStockArrayList1 == null){
                serverNotes.setText("Stock List is null, please try again later");
            }
            else {
                int size = finalStockArrayList1.size()-1;
                if(index[0] >= size){
                    index[0] = 0;
                    nextIndex[0] = 0;
                }
                else {
                    nextIndex[0] = index[0] + 1;
                    index[0] = nextIndex[0];
                }
                Stock object;
                object = finalStockArrayList1.get(nextIndex[0]);
                stockName.setText(object.getCompany());
                stockSymbol.setText(object.getSymbol());
                id_display.setText("ID: " + Math.toIntExact(object.getId()));
                curr_display.setText("" + (int) object.getCurrValue());

                getStockLogo(this.getApplicationContext(), Math.toIntExact(object.getId()));

                double stockPercentNum = (object.getPrevDayChange() / object.getCurrValue()) * 100;
                String stockPercent = String.format("%.3f", stockPercentNum);
                stockChange.setText("" + object.getPrevDayChange() + " || " + stockPercent + "%");
                if(object.getPrevDayChange() < 0){
                    stockImage.setImageResource(R.drawable.stockredarrow);
                }
                else{
                    stockImage.setImageResource(R.drawable.greenarrow);
                }
            }
        });
    }
    public void getStockLogo(Context context, int id) {
        ImageView stockLogo = findViewById(R.id.stockImageImageView);
        String URL_JSON_OBJECT = "http://10.90.75.130:8080/stocks/" + id +"/logo";
        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i(" full response", "getStockLogo: " + response);
                    //set image
                    try {
                        String url = response.getString("logo");
                        InputStream URLcontent = null;
                        try {
                            URLcontent = (InputStream) new URL(url).getContent();
                        }catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Drawable image = Drawable.createFromStream(URLcontent, "");
                        stockLogo.setImageDrawable(image);
                        
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },

                error -> Log.i("Error ", "getStockLogo: " + error.getMessage())) {
        };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }


    public ArrayList<Stock> getAllStocks(Context context) {
        String URL_JSON_OBJECT = "http://10.90.75.130:8080/stocks";
        ArrayList<Stock> stockArrayList = new ArrayList<>();
        stockName = findViewById(R.id.stockNameTextView);
        stockSymbol = findViewById(R.id.symbol_StockPage);
        id_display  = findViewById(R.id.stockID_view);
        curr_display  = findViewById(R.id.stockPrice_Display);
        stockChange = findViewById(R.id.stockChange_txt);
        stockImage = findViewById(R.id.stock_ImageView);

        //Create new request
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i(" full response", "getAllStocks: " + response.toString());

                    JSONObject object;
                    Stock stockObject;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            object = (JSONObject) response.get(i);
                            stockObject = new Stock();
                            stockObject.setCurrValue(object.getDouble("currValue"));
                            stockObject.setId(object.getLong("id"));
                            stockObject.setPrevDayChange(object.getDouble("prevDayChange"));
                            stockObject.setSymbol(object.getString("symbol"));
                            stockObject.setCompany(object.getString("company"));
                            stockArrayList.add(stockObject);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //parse first object
                    stockObject = stockArrayList.get(0);
                    stockName.setText(stockObject.getCompany());
                    stockSymbol.setText(stockObject.getSymbol());
                    id_display.setText("ID: " + Math.toIntExact(stockObject.getId()));
                    curr_display.setText("" + (int) stockObject.getCurrValue());

                    double stockPercentNum = (stockObject.getPrevDayChange() / stockObject.getCurrValue()) * 100;
                    String stockPercent = String.format("%.3f", stockPercentNum);
                    stockChange.setText("" + stockObject.getPrevDayChange() + " || " +  stockPercent + "%");
                    if(stockObject.getPrevDayChange() < 0){
                        stockImage.setImageResource(R.drawable.stockredarrow);
                    }
                    else{
                        stockImage.setImageResource(R.drawable.greenarrow);
                    }
                },

                error -> Log.i("Error ", "getAllStocks: " + error.getMessage())) {
        };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
        return stockArrayList;
    }

    public void SellStock(Context context, User user, int modelID) {
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
                error -> Log.i("Sell Stock error", "error: " + error)) { };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

    public void BuyStock(Context context, User user, int modelID) {
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/buy/" + modelID + "/user/" + user.getId() + "amt/1";

        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null,
                response -> {
                    Log.i("Buy Stock response", "response: " + response);

                },
                error -> Log.i("Buy Stock error", "error: " + error)) { };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
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
            Intent intent = new Intent(StockPage.this, NavPage.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.nav_stock){
            Intent intent = new Intent(StockPage.this, StockPage.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.nav_stock_list) {
            Intent intent = new Intent(StockPage.this, StockList.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.nav_login) {
            Intent intent = new Intent(StockPage.this, StartPage.class);
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

