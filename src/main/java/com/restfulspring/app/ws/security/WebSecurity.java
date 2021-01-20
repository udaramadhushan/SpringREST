 package com.restfulspring.app.ws.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.restfulspring.app.ws.service.UserService;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final UserService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public WebSecurity(UserService userDetailsService,BCryptPasswordEncoder bCryptPasswordEncoder) {
		
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userDetailsService = userDetailsService ;
		
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		http.csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
		.permitAll()
		.antMatchers(HttpMethod.GET, SecurityConstants.SIGN_UP_URL)
		.permitAll()
		.antMatchers(HttpMethod.GET, SecurityConstants.GET_USER)
		.permitAll()
		.antMatchers(HttpMethod.POST, SecurityConstants.REGISTER)
		.permitAll()
		.antMatchers(HttpMethod.GET, SecurityConstants.GET_ADDRESSES)
		.permitAll()
		.antMatchers(HttpMethod.GET , SecurityConstants.VERIFICATION_EMAIL_URL)
		.permitAll()
		.antMatchers(HttpMethod.POST , SecurityConstants.PASSWORD_RESET_REQ)
		.permitAll()
		.anyRequest().authenticated().and()
		.addFilter(getAuthenticationFilter())
		.addFilter(new AuthenticationFilter(authenticationManager()))
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	public AuthenticationFilter getAuthenticationFilter() throws Exception{
		
		final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
		filter.setFilterProcessesUrl("/users/login");
		return filter;
	}
}
