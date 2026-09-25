package com.example.stocktradingplatform.controller;

import	com.example.stocktradingplatform.model.Stock;
import	com.example.stocktradingplatform.model.StockPrice;
import	com.example.stocktradingplatform.service.StockPriceService;
import	org.springframework.web.bind.annotation.*;
import	java.time.LocalDateTime;
import	java.util.List;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins	=	"*")
public class StockController {

    private	final	StockPriceService	stockPriceService;
    public	StockController(StockPriceService	stockPriceService)	{
        this.stockPriceService	=	stockPriceService;
    }

    @GetMapping
    public	List<Stock>	getAllStocks()	{
        return	stockPriceService.getAllStocks();
    }

    @GetMapping("/{id}/latest-price")
    public	StockPrice	getLatestPrice(@PathVariable	Long	id)	{
        return	stockPriceService.getLatestPrice(id);
    }

    @GetMapping("/{id}/price-at")
    public	StockPrice	getPriceAt(@PathVariable	Long	id,	@RequestParam	String	dateTime)	{
        LocalDateTime	parsed	=	LocalDateTime.parse(dateTime);
        return	stockPriceService.getPriceAt(id,	parsed);
    }

    @GetMapping("/{id}/history")
    public	List<StockPrice>	getHistory(@PathVariable	Long	id)	{
        return	stockPriceService.getFullHistory(id);
    }
}
