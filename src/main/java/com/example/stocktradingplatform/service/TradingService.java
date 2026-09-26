package com.example.stocktradingplatform.service;

import	com.example.stocktradingplatform.model.*;
import	com.example.stocktradingplatform.repository.*;
import	org.springframework.stereotype.Service;
import	org.springframework.transaction.annotation.Transactional;
import	java.time.LocalDateTime;

@Service
public class TradingService {

    private	final	AppUserRepository	appUserRepository;
    private	final	HoldingRepository	holdingRepository;
    private	final	TransactionRepository	transactionRepository;
    private	final	StockPriceService	stockPriceService;
    public	TradingService(AppUserRepository	appUserRepository,
                             HoldingRepository	holdingRepository,
                             TransactionRepository	transactionRepository,
                             StockPriceService	stockPriceService)	{
        this.appUserRepository	=	appUserRepository;
        this.holdingRepository	=	holdingRepository;
        this.transactionRepository	=	transactionRepository;
        this.stockPriceService	=	stockPriceService;
    }

    private	AppUser	getDefaultUser()	{
        return	appUserRepository.findAll().stream().findFirst()
                .orElseThrow(()	->	new	RuntimeException("Default	user	not found!"));
    }
    @Transactional
    public	String	buyStock(Long	stockId,	Integer	quantity,	LocalDateTime	dateTime)	{
        if	(quantity	==	null	||	quantity	<=	0)	{
            throw	new	RuntimeException("Quantity	shouldn't more than 0");
        }
        AppUser	user	=	getDefaultUser();
        Stock	stock	=	stockPriceService.getStockById(stockId);
        StockPrice	priceAtTime	=	stockPriceService.getPriceAt(stockId,	dateTime);
        double	price	=	priceAtTime.getPrice();
        double	totalCost	=	price	*	quantity;
        if	(user.getVirtualBalance()	<	totalCost)	{
            throw	new	RuntimeException("You don't have that much money in your virtual balance!");
        }

        user.setVirtualBalance(user.getVirtualBalance()	-	totalCost);
        appUserRepository.save(user);

        Holding	holding	=	holdingRepository.findByUserAndStock(user,	stock).orElse(null);
        if	(holding	==	null)	{
            holding	=	new	Holding(user,	stock,	quantity,	price);
        }	else	{
            double	oldTotalCost	=	holding.getAverageBuyPrice()	*	holding.getQuantity();
            int	newQuantity	=	holding.getQuantity()	+	quantity;
            double	newAveragePrice	=	(oldTotalCost	+	totalCost)	/	newQuantity;
            holding.setQuantity(newQuantity);
            holding.setAverageBuyPrice(newAveragePrice);
        }
        holdingRepository.save(holding);
        //	Transaction	history	me	record	daalo
        Transaction	transaction	=	new	Transaction(user,	stock,	"BUY",	quantity,	price,	dateTime);
        transactionRepository.save(transaction);
        return	"You bought the "	+	quantity	+	" of shares	"	+	stock.getSymbol()	+	",	price	Rs."	+	price	+	" of each.";
    }
    @Transactional
    public	String	sellStock(Long	stockId,	Integer	quantity,	LocalDateTime	dateTime)	{
        if	(quantity	==	null	||	quantity	<=	0)	{
            throw	new	RuntimeException("Quantity	must be greater than 0");
        }
        AppUser	user	=	getDefaultUser();
        Stock	stock	=	stockPriceService.getStockById(stockId);
        Holding	holding	=	holdingRepository.findByUserAndStock(user,	stock)
                .orElseThrow(()	->	new	RuntimeException("You don't have stock of this share"));
        if	(holding.getQuantity()	<	quantity)	{
            throw	new	RuntimeException("You have only "	+	holding.getQuantity()
                    +	"	shares,	"	+	quantity	+	"	can't sell");
        }
        StockPrice	priceAtTime	=	stockPriceService.getPriceAt(stockId,	dateTime);
        double	price	=	priceAtTime.getPrice();
        double	totalValue	=	price	*	quantity;

        user.setVirtualBalance(user.getVirtualBalance()	+	totalValue);
        appUserRepository.save(user);

        int	remainingQuantity	=	holding.getQuantity()	-	quantity;
        if	(remainingQuantity	==	0)	{
            holdingRepository.delete(holding);
        }	else	{
            holding.setQuantity(remainingQuantity);
            holdingRepository.save(holding);
        }

        Transaction	transaction	=	new	Transaction(user,	stock,	"SELL",	quantity,	price,	dateTime);
        transactionRepository.save(transaction);
        return	"You sold "	+	quantity	+	"	shares	"	+	stock.getSymbol()	+	",	price	Rs."	+	price	+	"	each.";
    }
        }
