package com.example.as1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.as1.R;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        //Instantiate buttons
        Button toLogin_btn = findViewById(R.id.toLoginBtn);
        Button toCreateAccount_btn = findViewById(R.id.toCreateAccountBtn);
        TextView toPreviewStocks_btn = findViewById(R.id.toPreviewStocks_btn);
        //button to login page
        toLogin_btn.setOnClickListener(v -> {
            Intent intent = new Intent(StartPage.this, LoginPage.class);
            startActivity(intent);
        });

        //button to create account page
        toCreateAccount_btn.setOnClickListener(v -> {
            Intent intent = new Intent(StartPage.this, CreateAccountPage.class);
            startActivity(intent);
        });

        //button to stocks page
        toPreviewStocks_btn.setOnClickListener(v -> {
            Intent intent = new Intent(StartPage.this, StockList.class);
            startActivity(intent);
        });

    }


}