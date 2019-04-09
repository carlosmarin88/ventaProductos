package com.bolsadeideas.springboot.app.util;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utilitarios {
	
	private static final Logger logger = LoggerFactory.getLogger(Utilitarios.class);
	
	public static boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context==null) {
			return false;
		}
		
		Authentication auth = context.getAuthentication();
		
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		//lo que hace es validar contra el objecto si lo tiene
		return authorities.contains( new SimpleGrantedAuthority(role));
		
//		for(GrantedAuthority authority : authorities) {
//			if(role.equals(authority.getAuthority())) {
//				logger.info("El usuario ".concat(auth.getName()).concat(" tiene el role: ").concat(authority.getAuthority()));
//				return true;
//			}
//		}
//		
//		return false;
	}
}
