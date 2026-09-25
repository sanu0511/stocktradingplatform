package com.example.stocktradingplatform.repository;

import	com.example.stocktradingplatform.model.AppUser;
import	com.example.stocktradingplatform.model.Holding;
import	com.example.stocktradingplatform.model.Stock;
import	org.springframework.data.jpa.repository.JpaRepository;
import	java.util.List;
import	java.util.Optional;

public	interface	HoldingRepository	extends	JpaRepository<Holding,	Long>	{

    List<Holding>	findByUser(AppUser	user);
    Optional<Holding>	findByUserAndStock(AppUser	user,	Stock	stock);

}
