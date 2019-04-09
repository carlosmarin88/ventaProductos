package com.bolsadeideas.springboot.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccesHandler;
import com.bolsadeideas.springboot.app.models.service.impl.JpaUserDetailService;

//habilito el uso de anotaciones en los controlladores para los acceso a los recursos
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private LoginSuccesHandler successHandler;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JpaUserDetailService jpaUserDetailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//permito acceso a todas estas rutas
		//los dobles asterisco es una expresion regular que permite ver tambien todo su contenido de esa ruta
		//tambien poner /listar** es valido y cualquier url que matche sera valido
		http.authorizeRequests().antMatchers("/","/css/**","/js/**","/images/**","/listar**", "/api/**").permitAll()
		//se lo va establecer en los controller el acceso a los recursos
//		.antMatchers("/ver/**").hasAnyRole("USER")
//		.antMatchers("/uploads/**").hasAnyRole("USER")
//		.antMatchers("/form/**").hasAnyRole("ADMIN")
//		.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
//		.antMatchers("/factura/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and()//le digo la url del login
			.formLogin()
				.successHandler(successHandler)
				.loginPage("/login")
			.permitAll()
		.and()
		.logout().permitAll()
		//le digo la pagina de acceso denegado
		.and().exceptionHandling().accessDeniedPage("/error_403");
	/*
	 * login generado por el contexto spring page simple
	 * 	.formLogin().permitAll()
		.and()
		.logout().permitAll();

	 * 
	 * */
	
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder build) throws Exception{
		
		
		
		build.userDetailsService(jpaUserDetailService)
		.passwordEncoder(passwordEncoder);
		
		/*
		 * 	 CONECTANDO A LA BASE DE DATOS USANDO JDBC
		 */
		
		/*
		build.jdbcAuthentication().dataSource(dataSource)
		.passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("select username,password,enabled from users where username = ?")
		.authoritiesByUsernameQuery("select u.username, a.authority from authorities a "
				+ "inner join users u on (a.user_id=u.id) where u.username=?");
		*/
		/*
		 * GENERO USUARIOS EN MEMORIA PARA AUTENTIFICACION CON roles
		/*
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		UserBuilder users = User.builder().passwordEncoder(encoder::encode);
		
		build.inMemoryAuthentication().
		withUser(users.username("admin").password("12345").roles("ADMIN","USER")).
		withUser(users.username("carlos").password("12345").roles("USER"));
		
		*/
	}

}
