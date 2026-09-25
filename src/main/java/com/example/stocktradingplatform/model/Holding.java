package com.example.stocktradingplatform.model;

import	jakarta.persistence.*;

@Entity
@Table(name	=	"holdings")

public class Holding {

    @Id
    @GeneratedValue(strategy	=	GenerationType.IDENTITY)
    private	Long	id;
    @ManyToOne
    @JoinColumn(name	=	"user_id",	nullable	=	false)
    private	AppUser	user;
    @ManyToOne
    @JoinColumn(name	=	"stock_id",	nullable	=	false)
    private	Stock	stock;
    @Column(nullable	=	false)
    private	Integer	quantity;
    @Column(nullable	=	false)
    private	Double	averageBuyPrice;
    public	Holding()	{
    }
    public	Holding(AppUser	user,	Stock	stock,	Integer	quantity,	Double	averageBuyPrice)	{
        this.user	=	user;
        this.stock	=	stock;
        this.quantity	=	quantity;
        this.averageBuyPrice	=	averageBuyPrice;
    }
    public	Long	getId()	{
        return	id;
    }
    public	void	setId(Long	id)	{
        this.id	=	id;
    }
    public	AppUser	getUser()	{
        return	user;
    }
    public	void	setUser(AppUser	user)	{
        this.user	=	user;
    }
    public	Stock	getStock()	{
        return	stock;
    }
    public	void	setStock(Stock	stock)	{
        this.stock	=	stock;
    }
    public	Integer	getQuantity()	{
        return	quantity;
    }
    public	void	setQuantity(Integer	quantity)	{
        this.quantity	=	quantity;
    }
    public	Double	getAverageBuyPrice()	{
        return	averageBuyPrice;
    }
    public	void	setAverageBuyPrice(Double	averageBuyPrice)	{
        this.averageBuyPrice	=	averageBuyPrice;
    }
}
