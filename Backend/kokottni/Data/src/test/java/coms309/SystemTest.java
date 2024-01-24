package coms309;

import static org.junit.jupiter.api.Assertions.assertEquals;

import coms309.Stocks.Stock;
import coms309.Users.FriendGroup;
import coms309.Users.User;
import coms309.chat.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.UUID;

import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.boot.test.web.server.LocalServerPort;	// SBv3

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class SystemTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp(){
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    /*
    These tests below are all for the userController
     */

    @Test
    public void getAllUsersTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/users");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getAllStocksUserTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/user/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getUserByIdTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/users/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getUserByUsername(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/userByName/user");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    @Test
    public void purchaseStockTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/buy/4/user/3/amt/4");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            Double returnObj = returnArr.getDouble(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void sellStockTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/sell/4/user/3/4");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            Double returnObj = returnArr.getDouble(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getFriendGroupTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/friendgroup");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void createUserTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"dob\": \"08/21/2003\",\n" +
                        "    \"email\": \"sharif@iastate.edu\",\n" +
                        "    \"money\": 98765654,\n" +
                        "    \"name\": \"Sharif Mahdi\",\n" +
                        "    \"id\": 5,\n" +
                        "    \"username\": \"UsErname\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/users/5");

        //assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try {
            // Assuming the response is a JSON object, not an array
            JSONObject returnObj = new JSONObject(returnString);

            // Optionally, check if the response contains expected fields
            assertEquals("Sharif Mahdi", returnObj.getString("name"));
            assertEquals("UsErname", returnObj.getString("username"));
            // Add more assertions based on your response structure

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void loginTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json") // Update content type
                .body("{\n" +
                        "    \"username\": \"user\",\n" +
                        "    \"password\": \"Password\"\n" +
                        "}")
                .when()
                .post("/login");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try {
            // Assuming the response is a JSON object, not an array
            JSONObject returnObj = new JSONObject(returnString);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void createFriendGroupTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                post("/friendgroup/balls/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getUserFriendGroupTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/friendgroup/get/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getUsersFromGroupTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/friendgroup/getall/balls");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void addUserToGroupTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                put("/friendgroup/StockGroup1/1/2");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
            assertEquals("{\"message\":\"success\"}", returnObj);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void removeUserFromGroupTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                put("/friendgroup/remove/balls/1/2");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
            assertEquals("{\"message\":\"success\"}", returnObj);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void updateUserTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                put("/update/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void deleteUserTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                delete("/users/3");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
            assertEquals("{\"message\":\"success\"}", returnObj);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void createStockTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("{\n" +
                        "    \"id\": 47,\n" +
                        "    \"symbol\": \"AMZN\",\n" +
                        "    \"company\": \"Amazon\",\n" +
                        "    \"currValue\": 135.04,\n" +
                        "    \"prevDayChange\": 1.95\n" +
                        "}").
                when().
                post("/newstocks/1");

        //assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
            assertEquals("{\"message\":\"success\"}", returnObj);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void deleteStockTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                delete("/stocksc/3");

       assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void updateStocksTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                put("/update/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
            assertEquals("{\"message\":\"success\"}", returnObj);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void banUserTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                put("/banuser/3/byadmin/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void unbanUserTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                put("/unban/3/byadmin/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
            assertEquals("{\"message\":\"success\"}", returnObj);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    /*
    These following tests will be for the stock controller class
     */
    @Test
    public void getStocksTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/stocks");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getUsersForStocks(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/stock/1");

        //assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            assertEquals(0, returnArr.length());
//            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getAPIinfo(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/stocksUpdate/EXAS");

        assertEquals(500, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getStockById(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/stocks/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getCurrPrice(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/stockchange/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getStockNews(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/stocks/news/2");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getStockHistory(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/stocks/history/3");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void updateOneStock(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                put("/stocks/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void updateAllStocks(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                put("/stocks");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void userEqualsTest(){
        User user = new User(7L, 20, "Nick", "email", "dob", "user", "pass");
        User user2 = user;
        boolean functionOutput = user.equals(user2);
        assertEquals(true, functionOutput);
    }

    @Test
    public void userHashcodeTest(){
        User user = new User(7L, 20, "Nick", "email", "dob", "user", "pass");
        int code = user.hashCode();
        assertEquals(code, 38);
    }

    @Test
    public void stockEqualsTest(){
        Stock stock1 = new Stock(47L, "TSLA", "Tesla", 101.23, -2.13);
        assertEquals(false, stock1.equals(47L));
    }

    @Test
    public void stockHashcodeTest(){
        Stock stock1 = new Stock(47L, "TSLA", "Tesla", 101.23, -2.13);
        assertEquals(78, stock1.hashCode());
    }

    @Test
    public void friendGroupGetNameTest(){
        FriendGroup stockGroup1 = new FriendGroup(new UUID(0,0), "StockGroup1", 2);
        assertEquals("StockGroup1", stockGroup1.getGroupName());
    }
    @Test
    public void friendGroupGetGroupLeader(){
        FriendGroup stockGroup1 = new FriendGroup(new UUID(0,0), "StockGroup1", 2);
        assertEquals(2, stockGroup1.getGroupLeader());
    }

    @Test
    public void messageIDTest(){
        Message Message = new Message(0L, "Shiv", "Hello", "Nick");
        assertEquals(0, Message.getId());
    }
    @Test
    public void messageSentTest(){
        Message Message = new Message(0L, "Shiv", "Hello", "Nick");
        assertEquals("Shiv", Message.getUserName());
    }

    @Test
    public void messageTargetTest(){
        Message Message = new Message(0L, "Shiv", "Hello", "Nick");
        assertEquals("Nick", Message.getTarget());
    }

    @Test
    public void messageContentTest(){
        Message Message = new Message(0L, "Shiv", "Hello", "Nick");
        assertEquals("Hello", Message.getContent());
    }

    @Test
    public void setIdTest() {
        Message message = new Message();
        message.setId(1L);
        assertEquals(1L, message.getId());
    }

    @Test
    public void setUserTest() {
        Message message = new Message();
        message.setUserName("Shiv");
        assertEquals("Shiv", message.getUserName());
    }

    @Test
    public void setContentTest() {
        Message message = new Message();
        message.setContent("Hello");
        assertEquals("Hello", message.getContent());
    }

    @Test
    public void setNewLeaderTest(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                put("/friendgroup/setnewleader/name/2/3");

        assertEquals(500, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    @Test
    public void getFriendGroups(){
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                when().
                get("/chat/messages/friendgroup");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        try{
            JSONArray returnArr = new JSONArray(returnString);
            String returnObj = returnArr.getString(0);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


//    @Test
//    public void WebSocketConnect(){
//        Response response = RestAssured.given().
//                header("Sec-WebSocket-Version", "13").
//                header("Connection", "Upgrade").
//                header("Upgrade","websocket").
//                header("Sec-WebSocket-Extensions", "permessage-deflate; client_max_window_bits").
//                when().
//                get("/chat/Shiv/StockGroup1");
//
//        assertEquals(101, response.getStatusCode());
//
//        String returnString = response.getBody().asString();
//        try{
//            JSONArray returnArr = new JSONArray(returnString);
//            String returnObj = returnArr.getString(0);
//        }catch(JSONException e){
//            e.printStackTrace();
//        }
//    }

}
