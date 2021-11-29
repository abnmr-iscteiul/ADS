package com.example.demo;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {


	 @Override
	    protected void configure(HttpSecurity security) throws Exception
	    {
	     security.httpBasic().disable();
	    }
}
