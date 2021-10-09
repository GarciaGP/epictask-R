package br.com.fiap.epictask.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.fiap.epictask.repository.UserRepository;
import br.com.fiap.epictask.security.AuthorizationFilter;
import br.com.fiap.epictask.service.AuthenticationService;
import br.com.fiap.epictask.service.TokenService;

@Configuration
@Order(1)
public class SecurityConfigurationApi extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserRepository repository;
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService)
			.passwordEncoder(AuthenticationService.getPasswordEnconder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/api/**")
			.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/auth")
					.permitAll()
				.anyRequest()
					.authenticated()
				.and()
					.csrf()
					.disable()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
					.addFilterBefore(new AuthorizationFilter(tokenService, repository), UsernamePasswordAuthenticationFilter.class)
				;
	}

}
