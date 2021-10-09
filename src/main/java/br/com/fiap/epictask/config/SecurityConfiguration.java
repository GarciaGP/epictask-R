package br.com.fiap.epictask.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.fiap.epictask.service.AuthenticationService;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AuthenticationService authenticationService;

	// autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService)
			.passwordEncoder(AuthenticationService.getPasswordEnconder())
		;
	}
	
	//autorizacao
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/user/**")
				.hasRole("ADMIN")
			.antMatchers("/task/**")
				.authenticated()
			
			.anyRequest()
				.permitAll()
				
			.and()
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/task")
			.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")
			.and()
				.csrf()
				.disable()
			.headers().frameOptions().disable()
				
			;
	}

}
