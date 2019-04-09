package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long>{
	/**
	 * 
	 * realiza una llamada fetch haciendo que todas las consulta se resuelvan en un solo llamado
	 * @param id es el id de la factura
	 */
	//en la query se realizan varios fetch para obtener todo el objecto entero 
	//y el '?1' indica el parametro y se puede cargar mas de esa misma forma
	@Query("select f from Factura f join fetch f.cliente c join fetch f.items l join fetch l.producto where f.id =?1")
	public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id);
}
