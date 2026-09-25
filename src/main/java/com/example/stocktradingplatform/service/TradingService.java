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
                .orElseThrow(()	->	new	RuntimeException("Default	user	nahi	mila"));
    }
    @Transactional
    public	String	buyStock(Long	stockId,	Integer	quantity,	LocalDateTime	dateTime)	{
        if	(quantity	==	null	||	quantity	<=	0)	{
            throw	new	RuntimeException("Quantity	0	se	zyada	honi	chahiye");
        }
        AppUser	user	=	getDefaultUser();
        Stock	stock	=	stockPriceService.getStockById(stockId);
        StockPrice	priceAtTime	=	stockPriceService.getPriceAt(stockId,	dateTime);
        double	price	=	priceAtTime.getPrice();
        double	totalCost	=	price	*	quantity;
        if	(user.getVirtualBalance()	<	totalCost)	{
            throw	new	RuntimeException("Itna	paisa	nahi	hai	aapke	virtual	balance	me");
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
        return	"Aapne	"	+	quantity	+	"	shares	"	+	stock.getSymbol()	+	"	ke	khareede,	price	Rs."	+	price	+	"	each.";
    }
    @Transactional
    public	String	sellStock(Long	stockId,	Integer	quantity,	LocalDateTime	dateTime)	{
        if	(quantity	==	null	||	quantity	<=	0)	{
            throw	new	RuntimeException("Quantity	0	se	zyada	honi	chahiye");
        }
        AppUser	user	=	getDefaultUser();
        Stock	stock	=	stockPriceService.getStockById(stockId);
        Holding	holding	=	holdingRepository.findByUserAndStock(user,	stock)
                .orElseThrow(()	->	new	RuntimeException("Aapke	paas	is	stock	ke	shares	hain	hi	nahi"));
        if	(holding.getQuantity()	<	quantity)	{
            throw	new	RuntimeException("Aapke	paas	sirf	"	+	holding.getQuantity()
                    +	"	shares	hain,	"	+	quantity	+	"	nahi	bech	sakte");
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
        return	"Aapne	"	+	quantity	+	"	shares	"	+	stock.getSymbol()	+	"	ke	beche,	price	Rs."	+	price	+	"	each.";
    }
        }
