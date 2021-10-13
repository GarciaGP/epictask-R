package br.com.fiap.epictask.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.fiap.epictask.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class TokenService {

	@Value("${epictask.jwt.duration}")
	private long duration;
	
	@Value("${epictask.jwt.secret}")
	private String secret;

	public String createToken(Authentication authenticate) {
		
		User user = (User) authenticate.getPrincipal();
		
		Date today = new Date();
		Date expirationDate = new Date(today.getTime() + duration);
		
		String token = Jwts.builder()
								.setIssuer("EpicTaskAPI")
								.setSubject(user.getId().toString())
								.setIssuedAt(today)
								.setExpiration(expirationDate)
								.signWith(SignatureAlgorithm.HS256, secret)
							.compact();
		
		
		return token;
	}

	public boolean valid(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| IllegalArgumentException e) {
			return false;
		}
	}

	public Long getUserId(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return Long.valueOf( claims.getSubject() );
	}

}
