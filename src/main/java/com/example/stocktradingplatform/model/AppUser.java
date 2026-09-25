package com.example.stocktradingplatform.model;

import	jakarta.persistence.*;

@Entity
@Table(name	=	"app_user")

public class AppUser {
    @Id
    @GeneratedValue(strategy	=	GenerationType.IDENTITY)
    private	Long	id;
    @Column(nullable	=	false)
    private	String	name;
    @Column(nullable	=	false)
    private	Double	virtualBalance;
    public	AppUser()	{
    }
    public	AppUser(String	name,	Double	virtualBalance)	{
        this.name	=	name;
        this.virtualBalance	=	virtualBalance;
    }
    public	Long	getId()	{
        return	id;
    }
    public	void	setId(Long	id)	{
        this.id	=	id;
    }
    public	String	getName()	{
        return	name;
    }
    public	void	setName(String	name)	{
        this.name	=	name;
    }
    public	Double	getVirtualBalance()	{
        return	virtualBalance;
    }
    public	void	setVirtualBalance(Double	virtualBalance)	{
        this.virtualBalance	=	virtualBalance;
    }
}
