package com.example.stocktradingplatform.dto;

public class HoldingResponse {

    private	String	symbol;
    private	String	companyName;
    private	Integer	quantity;
    private	Double	averageBuyPrice;
    private	Double	currentPrice;
    private	Double	investedValue;
    private	Double	currentValue;
    private	Double	profitLoss;
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
    public	Double	getCurrentPrice()	{
        return	currentPrice;
    }
    public	void	setCurrentPrice(Double	currentPrice)	{
        this.currentPrice	=	currentPrice;
    }
    public	Double	getInvestedValue()	{
        return	investedValue;
    }
    public	void	setInvestedValue(Double	investedValue)	{
        this.investedValue	=	investedValue;
    }
    public	Double	getCurrentValue()	{
        return	currentValue;
    }
    public	void	setCurrentValue(Double	currentValue)	{
        this.currentValue	=	currentValue;
    }
    public	Double	getProfitLoss()	{
        return	profitLoss;
    }
    public	void	setProfitLoss(Double	profitLoss)	{
        this.profitLoss	=	profitLoss;
    }
}
