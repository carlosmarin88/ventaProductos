package com.bolsadeideas.springboot.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IProductoService;

@Controller
//esteblecto el permiso que tiene acceso a este recurso
@Secured("ROLE_ADMIN")
@RequestMapping("/factura")
//mantengo la factura en la session hasta que se procese en el formulario para persistir
@SessionAttributes("factura")
public class FacturaController {
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IProductoService productoService;
	
	private static final Logger logger = LoggerFactory.getLogger(FacturaController.class);
	
	
	@GetMapping("/form/{idCliente}")
	public String crear(@PathVariable(value="idCliente",required = true) Long idCliente,
		Model model, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(idCliente);
		if(cliente==null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		Factura factura = new Factura();
		factura.setCliente(cliente);
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Crear Factura");
		
		return "factura/form";
	}
	
	@GetMapping(value ="/cargar-productos/{term}", produces = {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable(value = "term" , required = true) String term){
		return productoService.findByNombre(term);
	}
	/**
	 * el sessionStatus se utiliza para matar la variable de sesion factura una vez que persistio
	 * @param factura
	 * @param itemsId
	 * @param cantidad
	 * @param flash
	 * @param status
	 * @return
	 */
	// el @valid lo que hace es habilitar las validaciones que tenga la entidad
	// el binding result nos permite comprobar si hubieron errores
	@PostMapping("/post")
	public String guardar(@Valid Factura factura, 
			BindingResult result,
			Model model,
			@RequestParam(name="item_id[]",required = false) Long[] itemsId,
			@RequestParam(name="cantidad[]", required = false)Integer[] cantidad,
			RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Factura");
			return "/factura/form";
		}
		
		if(itemsId == null || itemsId.length == 0) {
			model.addAttribute("error","La factura no puede no tener lineas!");
			model.addAttribute("titulo", "Crear Factura");
			return "/factura/form";
		}
		
		for(int x=0; x<itemsId.length; x++) {
			Producto producto = productoService.findProductoById(itemsId[x]);
			if(producto!=null) {
				ItemFactura linea = new ItemFactura();
				linea.setProducto(producto);
				linea.setCantidad(cantidad[x]);
				factura.addItemFactura(linea);
				
				logger.info("ID: " + itemsId[x].toString() + ", cantidad " + cantidad[x].toString());
			}
		}
		
		clienteService.saveFactura(factura);
		status.setComplete();
		flash.addFlashAttribute("success", "Factura guardada con exito!");
		
		return "redirect:/ver/" + factura.getCliente().getId();
	}
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value="id",required = true)Long id,
			Model model,
			RedirectAttributes flash) {
		
		Factura factura = clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProducto(id); //clienteService.findFacturaById(id);
		
		if(factura==null) {
			flash.addFlashAttribute("error", "La factura no existe en la base de datos!");
			return "redirect:/listar";
		}
		
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));
		return "factura/ver";
	}
	
	@GetMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id", required = true)Long id, RedirectAttributes flash) {
		
		Factura factura = clienteService.findFacturaById(id);
		
		if(factura!=null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura eliminada con exito!");
			return "redirect:/ver/"+factura.getCliente().getId();
		}
		flash.addFlashAttribute("error", "La factura no existe en la base de datos, no se pudo eliminar!");
		return "redirect:/listar";
	}
}
