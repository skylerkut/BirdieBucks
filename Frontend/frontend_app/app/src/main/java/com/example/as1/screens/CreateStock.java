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
import com.example.as1.Controllers.LoginAttempt;
import com.example.as1.Controllers.Stock;
import com.example.as1.Controllers.User;
import com.example.as1.ExternalControllers.VolleySingleton;
import com.example.as1.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateStock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_stock);

        Button cancel_btn = findViewById(R.id.cancel_btn);
        Button save_stock = findViewById(R.id.save_btn);
        EditText value_Display,company_Display,id_Display,symbol_Display;

        value_Display=findViewById(R.id.value_Display);
        company_Display=findViewById(R.id.company_Display);
        id_Display=findViewById(R.id.id_Display);
        symbol_Display=findViewById(R.id.symbol_Display);

        //cancel button
        cancel_btn.setOnClickListener(view -> {
            Intent intent = new Intent(CreateStock.this, AdminDashboardPage.class);
            startActivity(intent);
        });

        //save button
        save_stock.setOnClickListener(view -> {
            Stock stock = new Stock();
            stock.setCompany(company_Display.toString());
            stock.setSymbol(symbol_Display.toString());
            stock.setId(Long.valueOf(id_Display.toString()));
            stock.setCurrValue(Double.parseDouble(value_Display.toString()));

            postNewStock(getApplicationContext(), stock);
        });
    }

    public void postNewStock(Context context,  Stock stock){
        //TODO: change 1 to global user id
        String URL_JSON_OBJECT = "http://coms-309-051.class.las.iastate.edu:8080/newstocks/" + 1;
        //"http://coms-309-051.class.las.iastate.edu:8080/login";
        JSONObject objectBody = new JSONObject();

        // Convert input to JSONObject
        try {
            objectBody.put("id",stock.getId());
            objectBody.put("symbol",stock.getSymbol());
            objectBody.put("company",stock.getCompany());
            objectBody.put("currValue",stock.getCurrValue());
            Log.i("Stock JSON Object: ", objectBody.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Create new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL_JSON_OBJECT,
                objectBody,
                response -> {
                        if (response.equals( "{\"message\":\"success\"}")){
                        Intent intent = new Intent(CreateStock.this, AdminDashboardPage.class);
                        startActivity(intent);
                    }
                        else{
                            Log.i("Response", "postNewStock: response != success");
                        }
                },
                error -> Log.i("Login error", "error message: " + error)) { };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

}

