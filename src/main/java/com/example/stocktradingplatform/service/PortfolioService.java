package com.example.stocktradingplatform.service;

import	com.example.stocktradingplatform.dto.HoldingResponse;
import	com.example.stocktradingplatform.model.AppUser;
import	com.example.stocktradingplatform.model.Holding;
import	com.example.stocktradingplatform.model.StockPrice;
import	com.example.stocktradingplatform.repository.AppUserRepository;
import	com.example.stocktradingplatform.repository.HoldingRepository;
import	org.springframework.stereotype.Service;
import	java.util.List;
import	java.util.stream.Collectors;

@Service
public class PortfolioService {

    private	final	AppUserRepository	appUserRepository;
    private	final	HoldingRepository	holdingRepository;
    private	final	StockPriceService	stockPriceService;
    public	PortfolioService(AppUserRepository	appUserRepository,
                               HoldingRepository	holdingRepository,
                               StockPriceService	stockPriceService)	{
        this.appUserRepository	=	appUserRepository;
        this.holdingRepository	=	holdingRepository;
        this.stockPriceService	=	stockPriceService;
    }
    private	AppUser	getDefaultUser()	{
        return	appUserRepository.findAll().stream().findFirst()
                .orElseThrow(()	->	new	RuntimeException("Default	user	nahi	mila"));
    }
    public	Double	getVirtualBalance()	{
        return	getDefaultUser().getVirtualBalance();
    }
    public	List<HoldingResponse>	getPortfolio()	{
        AppUser	user	=	getDefaultUser();
        List<Holding>	holdings	=	holdingRepository.findByUser(user);
        return	holdings.stream().map(h	->	{
            StockPrice	latest	=	stockPriceService.getLatestPrice(h.getStock().getId());
            double	currentPrice	=	latest.getPrice();
            double	investedValue	=	h.getAverageBuyPrice()	*	h.getQuantity();
            double	currentValue	=	currentPrice	*	h.getQuantity();
            HoldingResponse	response	=	new	HoldingResponse();
            response.setSymbol(h.getStock().getSymbol());
            response.setCompanyName(h.getStock().getCompanyName());
            response.setQuantity(h.getQuantity());
            response.setAverageBuyPrice(h.getAverageBuyPrice());
            response.setCurrentPrice(currentPrice);
            response.setInvestedValue(investedValue);
            response.setCurrentValue(currentValue);
            response.setProfitLoss(currentValue	-	investedValue);
            return	response;
        }).collect(Collectors.toList());
    }
    public	double	getTotalProfitLoss()	{
        return	getPortfolio().stream().mapToDouble(HoldingResponse::getProfitLoss).sum();
    }
}
