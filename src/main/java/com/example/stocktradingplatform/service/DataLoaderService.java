package com.example.stocktradingplatform.service;

import	com.example.stocktradingplatform.model.Stock;
import	com.example.stocktradingplatform.model.StockPrice;
import	com.example.stocktradingplatform.repository.StockPriceRepository;
import	com.example.stocktradingplatform.repository.StockRepository;
import	org.springframework.beans.factory.annotation.Value;
import	org.springframework.boot.CommandLineRunner;
import	org.springframework.core.annotation.Order;
import	org.springframework.stereotype.Service;
import	java.io.BufferedReader;
import	java.io.File;
import	java.io.FileReader;
import	java.time.LocalDateTime;
import	java.time.format.DateTimeFormatter;
import	java.util.HashMap;
import	java.util.Map;

@Service
@Order(2)
public class DataLoaderService implements CommandLineRunner{

    private	final	StockRepository	stockRepository;
    private	final	StockPriceRepository	stockPriceRepository;
    @Value("${app.market-data.folder}")
    private	String	marketDataFolder;
    private	static	final	DateTimeFormatter	FORMATTER	=	DateTimeFormatter.ofPattern("yyyy-MM-dd	HH:mm");
    public	DataLoaderService(StockRepository	stockRepository,	StockPriceRepository	stockPriceRepository)	{
        this.stockRepository	=	stockRepository;
        this.stockPriceRepository	=	stockPriceRepository;
    }
    @Override
    public	void	run(String...	args)	throws	Exception	{

        if	(stockPriceRepository.count()	>	0)	{
            System.out.println("Stock	price	data already loaded. It can't load again.");
            return;
        }
        File	folder	=	new	File(marketDataFolder);
        if	(!folder.exists()	||	!folder.isDirectory())	{
            System.out.println("Can't found Market data folder :	"	+	folder.getAbsolutePath());
            System.out.println("First run DataGenerator.java!");
            return;
        }
        File[]	csvFiles	=	folder.listFiles((dir,	name)	->	name.toLowerCase().endsWith(".csv"));
        if	(csvFiles	==	null	||	csvFiles.length	==	0)	{
            System.out.println("Can't found	CSV	file in folder:	"	+	folder.getAbsolutePath());
            return;
        }
        Map<String,	Stock>	stockCache	=	new	HashMap<>();
        for	(File	file	:	csvFiles)	{

            String	symbol	=	file.getName().replace(".csv",	"").toUpperCase();
            Stock	stock	=	stockCache.get(symbol);
            if	(stock	==	null)	{
                stock	=	stockRepository.findBySymbol(symbol)
                        .orElseGet(()	->	stockRepository.save(new	Stock(symbol,	symbol	+	"	Limited")));
                stockCache.put(symbol,	stock);
            }
            try	(BufferedReader	br	=	new	BufferedReader(new	FileReader(file)))	{
                String	line;
                boolean	firstLine	=	true;
                int	count	=	0;
                while	((line	=	br.readLine())	!=	null)	{
                    if	(firstLine)	{
                        firstLine	=	false;
                        continue;
                    }
                    if	(line.trim().isEmpty())	{
                        continue;
                    }
                    String[]	parts	=	line.split(",");
                    String	date	=	parts[0].trim();
                    String	time	=	parts[1].trim();
                    double	price	=	Double.parseDouble(parts[2].trim());
                    LocalDateTime	dateTime	=	LocalDateTime.parse(date	+	"	"	+	time,	FORMATTER);
                    StockPrice	stockPrice	=	new	StockPrice(stock,	dateTime,	price);
                    stockPriceRepository.save(stockPrice);
                    count++;
                }
                System.out.println(count	+	"price for the "	+	symbol	+	" has been loaded in the row.");
            }
        }
        System.out.println("Market	data has been loaded into the database!");
    }
}
