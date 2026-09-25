package com.example.stocktradingplatform.repository;

import	com.example.stocktradingplatform.model.AppUser;
import	org.springframework.data.jpa.repository.JpaRepository;

public	interface	AppUserRepository	extends	JpaRepository<AppUser,	Long>	{
}
