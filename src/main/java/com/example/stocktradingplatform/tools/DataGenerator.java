package com.example.stocktradingplatform.tools;

import	java.io.File;
import	java.io.FileWriter;
import	java.io.IOException;
import	java.time.LocalDate;
import	java.time.LocalTime;
import	java.util.Random;

public class DataGenerator {

        //	10	stocks	ke	dummy	symbols
        private	static	final	String[]	SYMBOLS	=	{
                "TCS",	"INFY",	"RELI",	"HDFC",	"ICICI",
                "WIPRO",	"SBIN",	"ITC",	"TATAM",	"MARUTI"
        };
        //	Har	stock	ki	shuruwati	(starting)	price
        private	static	final	double[]	START_PRICES	=	{
                3500,	1500,	2800,	1650,	1050,
                420,	610,	440,	780,	11200
        };
        //	Kitne	trading	days	ka	data	chahiye	(Saturday/Sunday	chhod	kar)
        private	static	final	int	TRADING_DAYS	=	15;
        public	static	void	main(String[]	args)	throws	IOException	{
            File	folder	=	new	File("market-data");
            if	(!folder.exists())	{
                folder.mkdirs();
            }
            Random	random	=	new	Random();
            for	(int	s	=	0;	s	<	SYMBOLS.length;	s++)	{
                String	symbol	=	SYMBOLS[s];
                double	price	=	START_PRICES[s];
                File	file	=	new	File(folder,	symbol	+	".csv");
                try	(FileWriter	writer	=	new	FileWriter(file))	{
                    writer.write("Date,Time,Price\n");
                    LocalDate	date	=	LocalDate.of(2026,	1,	1);
                    int	daysGenerated	=	0;
                    while	(daysGenerated	<	TRADING_DAYS)	{
                        //	Saturday	(6)	aur	Sunday	(7)	ko	skip	karo	-	market	band	rehta	hai
                        if	(date.getDayOfWeek().getValue()	>=	6)	{
                            date	=	date.plusDays(1);
                            continue;
                        }
                        LocalTime	time	=	LocalTime.of(9,	15);
                        LocalTime	marketClose	=	LocalTime.of(15,	15);
                        while	(!time.isAfter(marketClose))	{
                            //	Random	walk:	price	thoda	upar	ya	thoda	neeche	jaayegi	(-1%	se	+1%	tak)
                            double	changePercent	=	(random.nextDouble()	-	0.5)	*	2;
                            price	=	price	+	(price	*	changePercent	/	100);
                            if	(price	<	1)	{
                                price	=	1;
                            }
                            String	line	=	date	+	","	+	time	+	","	+	String.format("%.2f",	price)	+	"\n";
                            writer.write(line);
                            time	=	time.plusMinutes(30);
                        }
                        date	=	date.plusDays(1);
                        daysGenerated++;
                    }
                }
                System.out.println("Generated	data	for	"	+	symbol	+	"	->	"	+	file.getPath());
            }
            System.out.println();
            System.out.println("Sabhi	CSV	files	'market-data'	folder	ke	andar	ban	chuki	hain!");
            System.out.println("Ab	StockTradingPlatformApplication.java	ko	run	karo.");
        }
}
