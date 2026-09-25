package com.example.stocktradingplatform.repository;

import	com.example.stocktradingplatform.model.Stock;
import	com.example.stocktradingplatform.model.StockPrice;
import	org.springframework.data.jpa.repository.JpaRepository;
import	java.time.LocalDateTime;
import	java.util.List;
import	java.util.Optional;
public	interface	StockPriceRepository	extends	JpaRepository<StockPrice,	Long>	{
    List<StockPrice>	findByStockOrderByPriceDateTimeAsc(Stock	stock);

    Optional<StockPrice>	findFirstByStockAndPriceDateTimeLessThanEqualOrderByPriceDateTimeDesc(
            Stock	stock,	LocalDateTime	dateTime);

    Optional<StockPrice>	findFirstByStockOrderByPriceDateTimeDesc(Stock	stock);
}
