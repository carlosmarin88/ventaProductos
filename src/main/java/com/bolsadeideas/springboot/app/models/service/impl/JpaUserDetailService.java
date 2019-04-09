package com.bolsadeideas.springboot.app.models.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.Usuario;

@Service("jpaUserDetailService")
public class JpaUserDetailService implements UserDetailsService{
	
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * cargar el usuario por su nombre
	 */
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario  usuario = usuarioDao.findByUsername(username);
		
		if(usuario==null) {
			logger.error("No existe el usuario '" + usuario + "'");
			throw new UsernameNotFoundException("el usuario " + username + " no existe en el sistema!");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(Role role : usuario.getAuthorities()) {
			/**
			 * registramos los roles que recuperamos 
			 */
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		
		if(authorities.isEmpty()) {
			logger.error("El usuario '" + username + "' no tiene roles asignado!");
			throw new UsernameNotFoundException("El usuario '" + username + "' no tiene roles asignado!");
		}
		
		return new User(usuario.getUsername(), usuario.getPassword(),
				usuario.getEnabled(), true, true, true, authorities);
	}

}
