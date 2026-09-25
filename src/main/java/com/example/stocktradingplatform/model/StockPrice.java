package com.example.stocktradingplatform.model;

import	jakarta.persistence.*;
import	java.time.LocalDateTime;

@Entity
@Table(name	=	"stock_prices")
public	class	StockPrice	{
    @Id
    @GeneratedValue(strategy	=	GenerationType.IDENTITY)
    private	Long	id;
    @ManyToOne
    @JoinColumn(name	=	"stock_id",	nullable	=	false)
    private	Stock	stock;
    @Column(nullable	=	false)
    private	LocalDateTime	priceDateTime;
    @Column(nullable	=	false)
    private	Double	price;
    public	StockPrice()	{
    }
    public	StockPrice(Stock	stock,	LocalDateTime	priceDateTime,	Double	price)	{
        this.stock	=	stock;
        this.priceDateTime	=	priceDateTime;
        this.price	=	price;
    }
    public	Long	getId()	{
        return	id;
    }
    public	void	setId(Long	id)	{
        this.id	=	id;
    }
    public	Stock	getStock()	{
        return	stock;
    }
    public	void	setStock(Stock	stock)	{
        this.stock	=	stock;
    }
    public	LocalDateTime	getPriceDateTime()	{
        return	priceDateTime;
    }
    public	void	setPriceDateTime(LocalDateTime	priceDateTime)	{
        this.priceDateTime	=	priceDateTime;
    }
    public	Double	getPrice()	{
        return	price;
    }
    public	void	setPrice(Double	price)	{
        this.price	=	price;
    }
}

