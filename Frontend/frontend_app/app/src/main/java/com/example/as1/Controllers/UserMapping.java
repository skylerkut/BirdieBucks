package com.example.as1.Controllers;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface UserMapping {



/*
    User Mapping
 */

    @GET("/users")
    Call<List<User>> getAllUsers();

    @GET("/userByName/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @GET("/user/{id}")
    Call<List<StockPurchased>> getAllStocksForUser(@Path("id") long id);

    @POST("/users")
    Call<User> createUser (@Body User user);

    @POST("/login")
    Call<User> login(@Body LoginAttempt login);

    @PUT("/users/{id}")
    Call<User> updateUser(@Body User updateUser);

    @DELETE("/users/{id}")
    Call<String> deleteUser(@Path("id") Long id);

/*
    Stock Mapping -- for user stocks
 */

    @GET("/buy/{id}/user/{uid}/amt/{amount}")
    Call<StockPurchased> purchaseStock(@Path("id") long id, @Path("uid") long uid, @Path("amount") int amount);

    @GET("/sell/{id}/user/{uid}/{numStocks}")
    Call<Double> sellStock(@Path("id") long id, @Path("uid") long uid, @Path("numStocks") int numStocks);

    @PUT("/users/{userId}/stocks/{stockId}/{numPurchasing}")
    Call<String> assignStockToUser(@Path("userID") Long userId, @Path("stockID") Long stockId, @Path("numPurchasing") int numPurchasing);

    @GET("/stocks/{uid}/{symbol}/{company}/{currValue}/{prevDayChange}")
    Call<String> createStock(@Path("uid") long uid, @Path("symbol") String symbol, @Path("company") String company,
                             @Path("currValue") double currValue, @Path("prevDayChange") double prevDayChange);

    @DELETE("/stocks/{sid}/{uid}")
    Call<String> deleteStock(@Path("sid") long sid, @Path("uid") long uid);

    @PUT("/update/{uid}")
    Call<String> updateAllStocks(@Path("uid") long uid);


    /*
        Group and Friend Group Mapping
     */

    @GET("/friendgroup/{groupName}")
    Call<List<String>> getGroupMembers(@Path("groupName") String groupName);

    @POST("/friendgroup/{groupName}")
    Call<FriendGroup> createFriendGroup(@Path("groupName") String groupName);

    //adds user userID to FriendGroup groupName
    @PUT("/friendgroup/{groupName}/{userID}/{gid}")
    Call<String> addUserToGroup(@Path("groupName") String groupName, @Path("userID") long userID, @Path("gid") long gid);

    //removes user userID from FriendGroup groupName
    @PUT("/friendgroup/remove/{gname}/{gid}/{uid}")
    Call<String> removeUserFromGroup(@Path("gname") String groupName, @Path("gid") long gid, @Path("uid") long uid);

    //Get list of friend groups
    @GET("/friendgroup")
    Call<List<FriendGroup>> getFriendGroups();

    //return a friend group from the server
    @GET("/friendgroup/get/{uid}")
    Call<FriendGroup> getFriendGroup(@Path("uid") long uid);

    //Get list of users that are group members
    @GET("/friendgroup/getall/{groupName}")
    Call<List<User>> getUsersFromGroup(@Path("groupName") String groupName);

    //Set Group leader
    @PUT("/friendgroup/setnewleader/{gname}/{gid}/{uid}")
    Call<String> setNewLeader(@Path("gname") String gname, @Path("gid") long gid, @Path("uid") long uid);



    /*
        Admin Only
     */

    @PUT("/banuser/{uid}/byadmin/{aid}")
    Call<String> banUser(@Path("uid") long uid, @Path("aid") long aid);

    @PUT("/unban/{uid}/byadmin/{aid}")
    Call<String> unbanUser(@Path("uid") long uid, @Path("aid") long aid);


    }
