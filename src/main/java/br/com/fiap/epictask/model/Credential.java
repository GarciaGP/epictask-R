package br.com.fiap.epictask.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Credential {
	
	@Email
	private String username;
	
	@NotBlank
	private String password;
	

}
