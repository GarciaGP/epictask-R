package br.com.fiap.epictask.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.epictask.model.User;
import br.com.fiap.epictask.repository.UserRepository;
import br.com.fiap.epictask.service.TokenService;

public class AuthorizationFilter extends OncePerRequestFilter {
	
	private TokenService tokenService;
	
	private UserRepository repository;
	
	public AuthorizationFilter(TokenService tokenService, UserRepository repository) {
		this.tokenService = tokenService;
		this.repository = repository;
	}

	@Override
	protected void doFilterInternal(
				HttpServletRequest request, 
				HttpServletResponse response, 
				FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = extractToken(request);
		
		boolean valid = tokenService.valid(token);
		
		if (valid) {
			authorize(token);
		}
		
		filterChain.doFilter(request, response);
	}

	private void authorize(String token) {
		Long id = tokenService.getUserId(token);
		User user = repository.findById(id).get();
		
		UsernamePasswordAuthenticationToken auth = 
				new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private String extractToken(HttpServletRequest request) {
		
		String header = request.getHeader("Authorization");
		
		if (header == null || header.isEmpty() || !header.startsWith("Bearer "))
			return null;
		
		return header.substring(7, header.length());
		
	}
	
}
