package br.com.fiap.epictask.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.epictask.model.Credential;
import br.com.fiap.epictask.service.TokenService;

@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;
	
	
	@PostMapping("/api/auth")
	public ResponseEntity<String> auth(@RequestBody Credential credential ) {
		UsernamePasswordAuthenticationToken auth = 
				new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword());
		
		try {
			Authentication authenticate = authManager.authenticate(auth);
			
			
			String token = tokenService.createToken(authenticate);
			
			return ResponseEntity.ok(token );
			
			
			
			
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
			
		}
		
	}

}
