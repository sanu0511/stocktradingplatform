package com.example.stocktradingplatform.model;

import	jakarta.persistence.*;
import	java.time.LocalDateTime;

@Entity
@Table(name	=	"transactions")
public class Transaction {

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
    private	String	type;	//	"BUY"	ya	"SELL"
    @Column(nullable	=	false)
    private	Integer	quantity;
    @Column(nullable	=	false)
    private	Double	price;
    @Column(nullable	=	false)
    private	LocalDateTime	transactionDateTime;
    public	Transaction()	{
    }
    public	Transaction(AppUser	user,	Stock	stock,	String	type,	Integer	quantity,
                          Double	price,	LocalDateTime	transactionDateTime)	{
        this.user	=	user;
        this.stock	=	stock;
        this.type	=	type;
        this.quantity	=	quantity;
        this.price	=	price;
        this.transactionDateTime	=	transactionDateTime;
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
    public	String	getType()	{
        return	type;
    }
    public	void	setType(String	type)	{
        this.type	=	type;
    }
    public	Integer	getQuantity()	{
        return	quantity;
    }
    public	void	setQuantity(Integer	quantity)	{
        this.quantity	=	quantity;
    }
    public	Double	getPrice()	{
        return	price;
    }
    public	void	setPrice(Double	price)	{
        this.price	=	price;
    }
    public	LocalDateTime	getTransactionDateTime()	{
        return	transactionDateTime;
    }
    public	void	setTransactionDateTime(LocalDateTime	transactionDateTime)	{
        this.transactionDateTime	=	transactionDateTime;
    }
    }
