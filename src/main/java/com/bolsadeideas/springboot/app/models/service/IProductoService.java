package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import com.bolsadeideas.springboot.app.models.entity.Producto;

public interface IProductoService {
	
	/**
	 * buscar los producto por nombre
	 * @param nombre
	 * @return te devuelve la busqueda parecida por el nombre pasado
	 */
	public List<Producto> findByNombre(String nombre);
	
	/**
	 * obtengo el producto por su id
	 * @param idProducto
	 * @return Producto obtenido
	 */
	public Producto findProductoById(Long idProducto);
}
