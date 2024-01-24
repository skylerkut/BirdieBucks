package coms309.Stocks;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockPurchasedRepository extends JpaRepository<StockPurchased, Long> {
    List<StockPurchased> findByStockId(long id);

    List<StockPurchased> findByUserId(long id);

    StockPurchased findById(long id);
}
