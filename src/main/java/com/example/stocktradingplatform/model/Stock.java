package com.example.stocktradingplatform.model;

import	jakarta.persistence.*;

@Entity
@Table(name	=	"stocks")
public	class	Stock	{
    @Id
    @GeneratedValue(strategy	=	GenerationType.IDENTITY)
    private	Long	id;
    @Column(unique	=	true,	nullable	=	false)
    private	String	symbol;
    @Column(nullable	=	false)
    private	String	companyName;
    public	Stock()	{
    }
    public	Stock(String	symbol,	String	companyName)	{
        this.symbol	=	symbol;
        this.companyName	=	companyName;
    }
    public	Long	getId()	{
        return	id;
    }
    public	void	setId(Long	id)	{
        this.id	=	id;
    }
    public	String	getSymbol()	{
        return	symbol;
    }
    public	void	setSymbol(String	symbol)	{
        this.symbol	=	symbol;
    }
    public	String	getCompanyName()	{
        return	companyName;
    }
    public	void	setCompanyName(String	companyName)	{
        this.companyName	=	companyName;
    }
}
