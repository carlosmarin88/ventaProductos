package com.bolsadeideas.springboot.app.models.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.springboot.app.models.service.IUploadFileService;

@Service("uploadFileService")
public class UploadFileServiceImpl implements IUploadFileService {

	private static final Logger logger = LoggerFactory.getLogger(UploadFileServiceImpl.class);
	
	private static final String UPLOADS_FOLDER = "uploads";
	
	@Override
	public Resource load(String filename)throws MalformedURLException {
		
		Path pathRoot = this.getPath(filename);
		logger.info("pathRoot: "  + pathRoot);
		Resource recurso = null;
		
		recurso = new UrlResource(pathRoot.toUri());
		
		if(!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error no se puede cargar la imagen: " + pathRoot.toString());	
		}
		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		
		String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = this.getPath(uniqueFileName);
		logger.info("rootPath: "  + rootPath);
		Files.copy(file.getInputStream(), rootPath);
		return uniqueFileName;
	}

	@Override
	public boolean delete(String filename) {
		
		Path rootPath = this.getPath(filename);
		File archivo = rootPath.toFile();
		
		if(archivo.exists() && archivo.canRead()) {
			if(archivo.delete()) {
				return true;
			}
		}
		
		return false;
	
	}
	
	private Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		Files.createDirectory(Paths.get(UPLOADS_FOLDER));
	}
	
}
