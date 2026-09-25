package com.example.stocktradingplatform.service;

import	com.example.stocktradingplatform.model.AppUser;
import	com.example.stocktradingplatform.repository.AppUserRepository;
import	org.springframework.beans.factory.annotation.Value;
import	org.springframework.boot.CommandLineRunner;
import	org.springframework.core.annotation.Order;
import	org.springframework.stereotype.Service;

@Service
@Order(1)
public class UserInitializerService implements CommandLineRunner {

    private final	AppUserRepository	appUserRepository;
    @Value("${app.initial-balance}")
    private	Double	initialBalance;
    public	UserInitializerService(AppUserRepository	appUserRepository)	{
        this.appUserRepository	=	appUserRepository;
    }
    @Override
    public	void	run(String...	args)	{

        if	(appUserRepository.count()	==	0)	{
            AppUser	user	=	new	AppUser("Demo	Trader",	initialBalance);
            appUserRepository.save(user);
            System.out.println("Default	user	'Demo	Trader'	ban	gaya.	Virtual	Balance:	"	+	initialBalance);
        }
    }
}
