package com.bolsadeideas.springboot.app.models.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.service.IProductoService;

@Service("productoService")
public class ProductoServiceImpl implements IProductoService {
	
	@Autowired
	private IProductoDao productoDao;

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String nombre) {
		return productoDao.findByNombreLikeIgnoreCase("%" + nombre + "%");
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long idProducto) {
		return productoDao.findById(idProducto).orElse(null);
	}
}
