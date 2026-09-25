package com.example.stocktradingplatform.dto;

public class BuySellRequest {

    private	Long	stockId;
    private	Integer	quantity;
    private	String	dateTime;	//	format:	2026-01-05T10:15
    public	Long	getStockId()	{
        return	stockId;
    }
    public	void	setStockId(Long	stockId)	{
        this.stockId	=	stockId;
    }
    public	Integer	getQuantity()	{
        return	quantity;
    }
    public	void	setQuantity(Integer	quantity)	{
        this.quantity	=	quantity;
    }
    public	String	getDateTime()	{
        return	dateTime;
    }
    public	void	setDateTime(String	dateTime)	{
        this.dateTime	=	dateTime;
    }
}
