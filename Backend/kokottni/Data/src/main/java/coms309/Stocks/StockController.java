package coms309.Stocks;

import java.util.List;

import coms309.Users.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.scheduling.annotation.Scheduled;

import coms309.Users.User;
import coms309.Users.UserRepository;

@Api(value = "StockController", description = "REST APIs related to the stock entity")
@RestController
public class StockController {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    UserRepository userRepository;

    StockUpdater stockAPI = new StockUpdater();

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Gets a list of all the stocks in the stockRepository", response = Iterable.class, tags = "stock")
    @GetMapping(path = "/stocks")
    List<Stock> getAllStocks(){return stockRepository.findAll();}

    @ApiOperation(value = "Gets all users single stock based on its id value from the stockRepository", response = Iterable.class, tags = "stock")
    @GetMapping(path = "/stock/{id}")
    List<StockPurchased> getAllUsersForStock(@PathVariable long id){return stockRepository.getOne(id).getUsers();}


    @ApiOperation(value = "Gets the live stock information from AlphaVantage to apply to one of our stocks so that current information is displayed.", response = String.class, tags = "stock")
    @GetMapping(path = "/stocksUpdate/{symbol}")
    String getStockAPIInfo(@PathVariable String symbol){
        return stockAPI.getUpdatedStockChange(symbol);
    }


    @ApiOperation(value = "Gets a stock to return based on its id", response = Stock.class, tags = "stock")
    @GetMapping(path = "/stocks/{id}")
    Stock getStockById(@PathVariable long id){return stockRepository.findById(id);}

    @ApiOperation(value = "Gets the current price from a stock based on its id", response = double.class, tags = "stock")
    @GetMapping(path = "/stockchange/{id}")
    double getCurrPrice(@PathVariable long id){return stockRepository.getOne(id).getCurrValue();}


    //retrieves news articles pertaining to stock (id)
    @ApiOperation(value = "Gets the current news for a stock from AlphaVantage API based on the id of that stock", response = String.class, tags = "stock")
    @GetMapping(path = "/stocks/news/{id}")
    String getStockNews(@PathVariable int id){
        return stockAPI.getStockNews(stockRepository.findById(id).getSymbol());
    }

    //retrieves weekly price history of stock (id)
    @ApiOperation(value = "Gets the price history of a stock from AlphaVantage API based on the id of the stock", response = String.class, tags = "stock")
    @GetMapping(path = "/stocks/history/{id}")
    String getStockHistory(@PathVariable int id){
        return stockAPI.getStockHistory(stockRepository.findById(id).getSymbol());
    }

    @GetMapping(path = "stocks/{id}/logo")
    String setStockLogo(@PathVariable int id){
        return stockAPI.setCompanyLogo(stockRepository, id);
    }

    @GetMapping(path = "stocks/{id}/website")
    String setStockSite(@PathVariable int id){
        return stockAPI.setCompanyWebsite(stockRepository, id);
    }

    @ApiOperation(value = "Creates a new stock that gets added to the stockRepository", response = String.class, tags = "stock")
    @PostMapping(path = "/stocks")
    String createStock(Stock stock){
        if(stock == null){
            return failure;
        }
        stockRepository.save(stock);
        return success;
    }


    @ApiOperation(value = "Updates a stock from the AlphaVantage API based on the stock id", response = String.class, tags = "stock")
    @PutMapping(path = "/stocks/{id}")
    String updateStockById(@PathVariable Long id){
        stockAPI.updateStockInfo(id, stockRepository);
        return success;
    }

    //updates stock repo every 15 min
    //TODO: alternate which 5 are being updated
    @ApiOperation(value = "Updates every stock in the stockRepostiory from the AlphaVantage API, based on current data", response = String.class, tags = "stock")
    //@Scheduled(fixedRate = 900000)
    @PutMapping(path = "/stocks")
    String updateAllStocks(){
        stockAPI.updateAllStocks(stockRepository);
        return success;
    }


    @ApiOperation(value = "Deletes a stock from the stockRepository based on its id", response = String.class, tags = "stock")
    @DeleteMapping(path = "/stocksc/{id}")
    String deleteStock(@PathVariable Long id){
        stockRepository.deleteById(id);
        return success;
    }
}
