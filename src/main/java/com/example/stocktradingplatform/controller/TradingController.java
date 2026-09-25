package com.example.stocktradingplatform.controller;

import	com.example.stocktradingplatform.dto.BuySellRequest;
import	com.example.stocktradingplatform.service.TradingService;
import	org.springframework.http.ResponseEntity;
import	org.springframework.web.bind.annotation.*;
import	java.time.LocalDateTime;
import	java.util.Map;

@RestController
@RequestMapping("/api/trade")
@CrossOrigin(origins	=	"*")
public class TradingController {

    private	final	TradingService	tradingService;
    public	TradingController(TradingService	tradingService)	{
        this.tradingService	=	tradingService;
    }
    //	POST	http://localhost:8080/api/trade/buy
    @PostMapping("/buy")
    public	ResponseEntity<?>	buy(@RequestBody	BuySellRequest	request)	{
        try	{
            LocalDateTime	dateTime	=	LocalDateTime.parse(request.getDateTime());
            String	message	=	tradingService.buyStock(request.getStockId(),	request.getQuantity(),	dateTime);
            return	ResponseEntity.ok(Map.of("message",	message));
        }	catch	(Exception	e)	{
            return	ResponseEntity.badRequest().body(Map.of("error",	e.getMessage()));
        }
    }
    //	POST	http://localhost:8080/api/trade/sell
    @PostMapping("/sell")
    public	ResponseEntity<?>	sell(@RequestBody	BuySellRequest	request)	{
        try	{
            LocalDateTime	dateTime	=	LocalDateTime.parse(request.getDateTime());
            String	message	=	tradingService.sellStock(request.getStockId(),	request.getQuantity(),	dateTime);
            return	ResponseEntity.ok(Map.of("message",	message));
        }	catch	(Exception	e)	{
            return	ResponseEntity.badRequest().body(Map.of("error",	e.getMessage()));
        }
    }
}
