package coms309.Stocks;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class StockUpdater{

//    Alpha Vantage API Key: UOICPFOUUT832ZST
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private static final String CLEARBIT_API_KEY = "sk_2f3b74072915083225411ed6e34a9ba1"; // Replace with your actual Clearbit API key

    private final Logger logger = LoggerFactory.getLogger(StockUpdater.class);

    public String setCompanyWebsite(StockRepository repo, long stockId){
        Stock stock = repo.findById(stockId);
        stock.setCompanyLogo("https://logo.clearbit.com/" + stock.getCompanyWebsite());
        repo.save(stock);
        return "{\"logo\":\"" + stock.getCompanyLogo() + "\"}";
    }
    public String setCompanyLogo(StockRepository repo, long stockId) {
        try {
            String apiRequest = "https://company.clearbit.com/v1/domains/find?name=" + repo.findById(stockId).getCompany().replaceAll(" ", "");
            System.out.println(apiRequest);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiRequest))
                    .header("Authorization", "Bearer " + CLEARBIT_API_KEY)
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                int logoIndex = jsonResponse.indexOf("\"logo\":");
                if (logoIndex != -1) {
                    int startUrlIndex = jsonResponse.indexOf("\"", logoIndex + 7) + 1;
                    int endUrlIndex = jsonResponse.indexOf("\"", startUrlIndex);
                    Stock stock = repo.findById(stockId);
                    stock.setCompanyLogo(jsonResponse.substring(startUrlIndex, endUrlIndex));
                    repo.save(stock);
                    return "{\"logo\":\"" + jsonResponse.substring(startUrlIndex, endUrlIndex) + "\"}";
                }
                else{
                    return setCompanyWebsite(repo, stockId);
                }
                //return response.body();
            } else {
                return setCompanyWebsite(repo, stockId);
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getStockNews(String symbol){
        try{
            String apiRequest = "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=" + symbol + "&apikey=JPXFS631336A8RE8";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiRequest))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return null;
            }
        }
        catch(IOException | InterruptedException | java.net.URISyntaxException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getStockHistory(String symbol){
        try{
            String apiRequest = "https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol=" + symbol + "&apikey=JPXFS631336A8RE8";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiRequest))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return null;
            }
        }
        catch(IOException | InterruptedException | java.net.URISyntaxException e){
            e.printStackTrace();
            return null;
        }
    }

    public String updateStockData(String symbol){
        try{
            String apiRequest = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=JPXFS631336A8RE8";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiRequest))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return null;
            }
        } catch (IOException | InterruptedException | java.net.URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

    }

    //parameter is a call to updateStockData function
    public String getUpdatedStockPrice(String stockInfo){

        int priceIndex = stockInfo.indexOf("\"05. price\":");
        String t = stockInfo.substring((priceIndex+14));
        int endPriceIndex = t.indexOf("\"");
        return stockInfo.substring((priceIndex+14), (priceIndex+14) + endPriceIndex);
    }

    public String getUpdatedStockChange(String stockInfo){
        int priceIndex = stockInfo.indexOf("\"10. change percent\": ");
        String t = stockInfo.substring((priceIndex+23));
        int endPriceIndex = t.indexOf("\"");
        return stockInfo.substring((priceIndex+23), (priceIndex+22) + endPriceIndex);
    }

    public String updateStockInfo(Long id, StockRepository repo){
        Stock stockToUpdate = repo.getOne(id);
        String stockInfo = updateStockData(stockToUpdate.getSymbol());
        stockToUpdate.setCurrValue(Double.valueOf(getUpdatedStockPrice(stockInfo)));
        stockToUpdate.setPrevDayChange(Double.valueOf(getUpdatedStockChange(stockInfo)));
        repo.save(stockToUpdate);
        return "Success";
    }


    //TODO Update in batches of 5 because of api rate limit 5 per minute


public void updateAllStocks(StockRepository repo) {
    logger.info("Scheduled task started.");
    try {
        List<Stock> allStocks = repo.findAll();
        for (Stock stock : allStocks) {
            String stockInfo = updateStockData(stock.getSymbol());
            stock.setCurrValue(Double.valueOf(getUpdatedStockPrice(stockInfo)));
            stock.setPrevDayChange(Double.valueOf(getUpdatedStockChange(stockInfo)));
            repo.save(stock);
        }
        logger.info("Scheduled task completed successfully.");
    } catch (Exception e) {
        logger.error("Scheduled task encountered an error.", e);
    }
}


}