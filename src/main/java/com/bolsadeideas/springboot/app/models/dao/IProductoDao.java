package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {
	
	//uso query de JPA para consultar y paso el parametro al like
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String nombre);
	
	//busqueda por spring data por nombre del metodo
	public List<Producto> findByNombreLikeIgnoreCase(String nombre);
}
