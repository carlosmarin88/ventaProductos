package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.util.Utilitarios;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
//guardamos el cliente en la sesion
@SessionAttributes("cliente")
public class ClienteController {
	
	@Autowired
	//@Qualifier("clienteDaoJpa")
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//esteblecto el permiso que tiene acceso a este recurso
	//@Secured("ROLE_USER")
	//de esta manera valido mas de un solo role
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value = "id", required = true)Long id, Model model, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.fetchByIdWithFacturas(id); //clienteService.findOne(id);
		if(cliente ==null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Detalle cliente: " + cliente.getNombre());
		return "ver";
	}
	
	//@RequestMapping(value = {"/listar","/"}, method = RequestMethod.GET)
	@GetMapping({"/listar","/"})
	public String listar(@RequestParam(name="page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {
		
		if(authentication!=null) {
			logger.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//obtener el principal por securityContextHolder
		if(auth!=null) {
			logger.info("Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(),"
					+ " Usuario autenticado:  ".concat(auth.getName()));
		}
		
		if(Utilitarios.hasRole("ROLE_ADMIN")) {
			logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		}else {
			logger.warn("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		
		if(securityContext.isUserInRole("ADMIN")) {
			logger.info("Utilizando usando SecurityContextHolderAwareRequestWrapper ".concat("tiene acceso!"));
		}else {
			logger.warn("Utilizando usando SecurityContextHolderAwareRequestWrapper ".concat("NO tiene acceso!"));
		}
		
		//de esta manera se hace obligatorio tener el role completo para comparar
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("Utilizando usando request de forma nativa ".concat("tiene acceso!"));
		}else {
			logger.warn("Utilizando usando request de forma nativa ".concat("NO tiene acceso!"));
		}
		
		Pageable pageRequest = PageRequest.of(page,4);
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		model.addAttribute("titulo", "Listado de clientes");
		//model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("clientes", clienteService.findAll(pageRequest));
		model.addAttribute("page", pageRender);
		
		return "listar";
	}
	
	
	//@RequestMapping(value = {"/listar","/"}, method = RequestMethod.GET)
	@RequestMapping(method= RequestMethod.GET, value="listar-rest")
	public @ResponseBody List<Cliente> listarRest() {
		return clienteService.findAll();
	}
	
	//esteblecto el permiso que tiene acceso a este recurso
	@Secured("ROLE_ADMIN")
	@GetMapping(value="/form")
	public String crear(Map<String, Object> model) {
		model.put("titulo", "Formulario de cliente");
		model.put("cliente", new Cliente());
		return "form";
	}
	
	
	//esteblecto el permiso que tiene acceso a este recurso
	//@Secured("ROLE_ADMIN")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = null;
		
		if(id>0) {
			cliente = clienteService.findOne(id);
			if(cliente==null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la base de datos");
				return "redirect:/listar";
			}
		}else {
			flash.addFlashAttribute("error","El id del cliente no puede ser 0");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}
	
	
	//esteblecto el permiso que tiene acceso a este recurso
	@Secured("ROLE_ADMIN")
	//agrego annotation de validacion para cliente
	//los mensaje de error se encuentran personalizado en el archivo messages.properties
	@PostMapping(value="/form")
	public String guardar(@Valid Cliente cliente , BindingResult result, Model model,@RequestParam("file") MultipartFile foto,
			RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		
		if(!foto.isEmpty()) {
			
			
			if(cliente.getId()!=null 
					&& cliente.getId()>0
					&& cliente.getFoto()!= null
					&& cliente.getFoto().length() > 0) {
				
				uploadFileService.delete(cliente.getFoto());
				
			}
			
			try {
				
				String uniqueFileName = uploadFileService.copy(foto);
				
				flash.addFlashAttribute("info", "Ha subido correctamente '" + uniqueFileName + "'");
				cliente.setFoto(uniqueFileName);
			} catch (IOException e) {
				System.out.println("Error al cargar la foto: " + e.getMessage());
			}
		}
		
		String mensaje = (cliente.getId()!= null) ? "Cliente editado con exito!" : "Cliente creado con exito!";
		clienteService.save(cliente);
		//elimino el cliente de la session
		status.setComplete();
		flash.addFlashAttribute("success",mensaje);
		return "redirect:listar";
	}
	
	//esteblecto el permiso que tiene acceso a este recurso
	@Secured("ROLE_ADMIN")
	@GetMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id", required=true)Long id, RedirectAttributes flash) {
		
		if(id>0) {
			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success","Cliente eliminado con exito!");
			
			if(uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info","Foto " + cliente.getFoto() + " eliminada con exito!");
			}
			
		}
		return "redirect:/listar";
	}
	
	//esteblecto el permiso que tiene acceso a este recurso
	// puede recibir mas de un log para validar
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	//:.+ es una expresion regular que permite las extensiones enviar .png, de lo contrario lo cortaria 
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable(value="filename", required = true) String filename){

		Resource recurso = null;				
		try {
			 recurso = uploadFileService.load(filename);			 
		} catch (MalformedURLException e) {
			logger.error("La url esta mal formada: " + e.getMessage());
		}
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() +"\"")
				.body(recurso);
		
	}
}
