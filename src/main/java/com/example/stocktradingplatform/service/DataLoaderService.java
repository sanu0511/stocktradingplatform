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
        //	Agar	data	pehle	se	load	hai,	to	dobara	load	mat	karo	(restart	karne	par	duplicate	na	ho)
        if	(stockPriceRepository.count()	>	0)	{
            System.out.println("Stock	price	data	pehle	se	load	hai.	Dobara	load	nahi	kar	raha.");
            return;
        }
        File	folder	=	new	File(marketDataFolder);
        if	(!folder.exists()	||	!folder.isDirectory())	{
            System.out.println("Market	data	folder	nahi	mila:	"	+	folder.getAbsolutePath());
            System.out.println("Pehle	DataGenerator.java	ko	run	karo!");
            return;
        }
        File[]	csvFiles	=	folder.listFiles((dir,	name)	->	name.toLowerCase().endsWith(".csv"));
        if	(csvFiles	==	null	||	csvFiles.length	==	0)	{
            System.out.println("Koi	CSV	file	nahi	mili	folder	me:	"	+	folder.getAbsolutePath());
            return;
        }
        Map<String,	Stock>	stockCache	=	new	HashMap<>();
        for	(File	file	:	csvFiles)	{
            //	File	ka	naam	(bina	.csv	ke)	hi	stock	symbol	hoga,	jaise	"TCS.csv"	->	"TCS"
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
                System.out.println(symbol	+	"	ke	liye	"	+	count	+	"	price	rows	load	ho	gayi.");
            }
        }
        System.out.println("Market	data	database	me	load	ho	gaya!");
    }
}
