package com.bolsadeideas.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {

	public Resource load(String filename) throws MalformedURLException;
	
	/**
	 * guardar el archivo
	 * @param file 
	 * @return devuelve el nombre del archivo modificado
	 * @throws IOException
	 */
	public String copy(MultipartFile file) throws IOException;
	
	public boolean delete(String filename);
	
	/**
	 * eliminar en forma recursiva
	 */
	public void deleteAll();
	
	/**
	 * inicializador, volver a crear el directorio uploads
	 * @throws IOException
	 */
	public void init() throws IOException;
}
