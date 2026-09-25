package com.example.stocktradingplatform.repository;

import	com.example.stocktradingplatform.model.AppUser;
import	com.example.stocktradingplatform.model.Transaction;
import	org.springframework.data.jpa.repository.JpaRepository;
import	java.util.List;

public	interface	TransactionRepository	extends	JpaRepository<Transaction,	Long>	{

    List<Transaction>	findByUserOrderByTransactionDateTimeDesc(AppUser	user);

}
