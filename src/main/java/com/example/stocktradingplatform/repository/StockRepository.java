package com.example.stocktradingplatform.repository;

import	com.example.stocktradingplatform.model.Stock;
import	org.springframework.data.jpa.repository.JpaRepository;
import	java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findBySymbol(String symbol);
}
