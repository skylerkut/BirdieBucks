package com.example.as1.Controllers;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StockMapping {

        @GET("/stocks")
        Call<List<Stock>> getAllStocks();

        @GET("/stock/{id}")
        Call<List<StockPurchased>> getAllUsersForStock(@Path("id") long id);

        @GET("/stocksUpdate/{symbol}")
        Call<Stock> getStockAPIInfo(@Path("symbol") String symbol);

        @GET("/stocks/{id}")
        Stock getStockById(@Path("id") Long id);

        @GET("/stockchange/{id}")
        Call<Stock> getCurrPrice(@Path("id") Long id);

    @POST("/stocks")
        Call<Stock> createStock(Stock stock);

    @POST("/stocks/{id}")
    Call<Stock> updateStockById(@Path("id") int id);

    //retrieves news articles pertaining to stock (id)
    @GET("/stocks/news/{id}")
    Call<String> getStockNews(@Path("id") int id);

    //retrieves weekly price history of stock (id)
    @GET("/stocks/history/{id}")
    Call<String> getStockHistory(@Path("id") int id);

        @DELETE("/stocks/{id}")
        Call<Stock> deleteStock(@Path("id") Long id);

    @GET("stocks/{id}/logo")
    Call<String> setStockLogo(@Path("id") int id);

    }
