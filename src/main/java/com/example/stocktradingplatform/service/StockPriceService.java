package com.example.stocktradingplatform.service;

import	com.example.stocktradingplatform.model.Stock;
import	com.example.stocktradingplatform.model.StockPrice;
import	com.example.stocktradingplatform.repository.StockPriceRepository;
import	com.example.stocktradingplatform.repository.StockRepository;
import	org.springframework.stereotype.Service;
import	java.time.LocalDateTime;
import	java.util.List;

@Service
public class StockPriceService {

    private	final	StockRepository	stockRepository;
    private	final	StockPriceRepository	stockPriceRepository;
    public	StockPriceService(StockRepository	stockRepository,	StockPriceRepository	stockPriceRepository)	{
        this.stockRepository	=	stockRepository;
        this.stockPriceRepository	=	stockPriceRepository;
    }
    public	List<Stock>	getAllStocks()	{
        return	stockRepository.findAll();
    }
    public	Stock	getStockById(Long	id)	{
        return	stockRepository.findById(id)
                .orElseThrow(()	->	new	RuntimeException("Stock	nahi	mila,	id:	"	+	id));
    }

    public	StockPrice	getPriceAt(Long	stockId,	LocalDateTime	dateTime)	{
        Stock	stock	=	getStockById(stockId);
        return	stockPriceRepository
                .findFirstByStockAndPriceDateTimeLessThanEqualOrderByPriceDateTimeDesc(stock,	dateTime)
                .orElseThrow(()	->	new	RuntimeException(
                        "Is	date/time	se	pehle	is	stock	ka	koi	price	data	available	nahi	hai"));
    }

    public	StockPrice	getLatestPrice(Long	stockId)	{
        Stock	stock	=	getStockById(stockId);
        return	stockPriceRepository.findFirstByStockOrderByPriceDateTimeDesc(stock)
                .orElseThrow(()	->	new	RuntimeException("Is	stock	ka	koi	price	data	nahi	mila"));
    }
    public	List<StockPrice>	getFullHistory(Long	stockId)	{
        Stock	stock	=	getStockById(stockId);
        return	stockPriceRepository.findByStockOrderByPriceDateTimeAsc(stock);
    }
}
