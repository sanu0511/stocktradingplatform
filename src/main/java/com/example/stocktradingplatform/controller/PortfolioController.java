package com.example.stocktradingplatform.controller;

import	com.example.stocktradingplatform.dto.HoldingResponse;
import	com.example.stocktradingplatform.model.Transaction;
import	com.example.stocktradingplatform.repository.AppUserRepository;
import	com.example.stocktradingplatform.repository.TransactionRepository;
import	com.example.stocktradingplatform.service.PortfolioService;
import	org.springframework.web.bind.annotation.*;
import	java.util.List;
import	java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins	=	"*")
public class PortfolioController {

    private	final	PortfolioService	portfolioService;
    private	final	TransactionRepository	transactionRepository;
    private	final	AppUserRepository	appUserRepository;
    public	PortfolioController(PortfolioService	portfolioService,
                                  TransactionRepository	transactionRepository,
                                  AppUserRepository	appUserRepository)	{
        this.portfolioService	=	portfolioService;
        this.transactionRepository	=	transactionRepository;
        this.appUserRepository	=	appUserRepository;
    }
    //	GET	http://localhost:8080/api/portfolio
    @GetMapping("/portfolio")
    public	List<HoldingResponse>	getPortfolio()	{
        return	portfolioService.getPortfolio();
    }
    //	GET	http://localhost:8080/api/balance
    @GetMapping("/balance")
    public	Map<String,	Double>	getBalance()	{
        return	Map.of(
                "virtualBalance",	portfolioService.getVirtualBalance(),
                "totalProfitLoss",	portfolioService.getTotalProfitLoss()
        );
    }
    //	GET	http://localhost:8080/api/transactions
    @GetMapping("/transactions")
    public	List<Transaction>	getTransactions()	{
        return	transactionRepository.findByUserOrderByTransactionDateTimeDesc(
                appUserRepository.findAll().get(0));
    }
}
