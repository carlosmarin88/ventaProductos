package com.bolsadeideas.springboot.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

	private static Logger logger = LoggerFactory.getLogger(MvcConfig.class);
	
	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		String resourcePath =Paths.get("uploads").toAbsolutePath().toUri().toString();
		logger.info("resourceAbsolutePath: " + resourcePath); 
		//registrar un directorio fuera del proyecto
		registry.addResourceHandler("/uploads/**")//ver cualquier archivo o subcarpeta adentro
		.addResourceLocations(resourcePath);//direccion fisica del nuevo directorio
	}*/
	
	/**
	 * un registro de las vista
	 */
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403").setViewName("error_403");
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(new Class[] {com.bolsadeideas.springboot.app.view.xml.ClienteList.class});
		return marshaller;
	}

}
