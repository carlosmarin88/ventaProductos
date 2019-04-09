package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;

public interface IClienteService {
	
	public List<Cliente> findAll();
	
	//page es casi lo mismo que list
	public Page<Cliente> findAll(Pageable pageable);
	
	public void save(Cliente cliente);
	
	public Cliente findOne(Long id);
	
	/**
	 * busca el cliente con todas sus fuctura de modo eager con fetch
	 * @param id
	 * @return
	 */
	public Cliente fetchByIdWithFacturas(Long id);
	
	public void delete(Long id);
	
	public void saveFactura(Factura factura);
	
	public Factura findFacturaById(Long id);
	
	public void deleteFactura(Long id);
	/**
	 * se pasa el id de la factura
	 * @param id
	 * @return Factura de forma eager con todos los campos cargado
	 */
	public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id);

}
