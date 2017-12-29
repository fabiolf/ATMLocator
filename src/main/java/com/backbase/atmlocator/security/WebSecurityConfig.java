package com.backbase.atmlocator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("configure HttpSecurity");
		http.httpBasic()
			.and().authorizeRequests()
				.antMatchers("/api/**").hasRole("USER")
				.antMatchers("/**").hasRole("ADMIN")
			.and().csrf().disable()
			.headers().frameOptions().disable();

//		http.authorizeRequests()
//			.antMatchers("/","/js/**","/css/**","/img/**","/fonts/**","/lib/**","/api/user/authenticate").permitAll()
//			.anyRequest().authenticated()
//			.and()
//		.formLogin()
//			.loginPage("/")
//			.permitAll()
//			.and()
//		.logout()
//			.permitAll();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		System.out.println("configure AuthenticationManagerBuilder");
		auth.inMemoryAuthentication().withUser("user1").password("secret1").roles("USER")
			.and().withUser("admin1").password("secret1").roles("USER", "ADMIN");
	}
}