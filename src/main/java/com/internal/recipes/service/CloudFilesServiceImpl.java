package com.internal.recipes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.internal.recipes.domain.CloudFilesObject;
import com.internal.recipes.exception.CloudFilesException;
import com.rackspacecloud.client.cloudfiles.FilesClient;
import com.rackspacecloud.client.cloudfiles.FilesObject;


@Service
public class CloudFilesServiceImpl implements CloudFilesService {
	
	private static final Logger logger = LoggerFactory.getLogger(CloudFilesService.class);
	
	private @Value("${cloudFiles.apiUsername}") String apiUsername;
	private @Value("${cloudFiles.apiKey}") String apiKey;
	private @Value("${cloudFiles.connection_timeout}") int connectionTimeout;
	private @Value("${cloudFiles.auth_url}") String authUrl;
	private @Value("${cloudFiles.rootContainer}") String rootContainer;
	
	private FilesClient filesClient = null;
	
	
	@PostConstruct
	public void init()  {
		logger.info("CloudFilesServiceImpl init:: connecting to CloudFiles");
		filesClient = new FilesClient(apiUsername, apiKey, authUrl, "", connectionTimeout);
		try {
			filesClient.login();
		} catch (Exception e) {
			logger.info("CloudFilesServiceImpl ctor:: failed to make a connection to CloudFiles, error: {}", e.getMessage());
			throw new CloudFilesException(e.getMessage());
		}
	}


	public CloudFilesObject storeObject(MultipartFile file, String path, String filename) {	   
	    CloudFilesObject cfo = new CloudFilesObject();

	    try {
		    filesClient.storeObject(rootContainer, file.getBytes(), file.getContentType(), path + "/" + filename, new HashMap<String,String>());
		    List<FilesObject> fileObjects = filesClient.listObjectsStartingWith(rootContainer, path + "/" + filename, null, 1, null);
			FilesObject fo = fileObjects.get(0);		    
			cfo.setName(fo.getName());
			cfo.setLastModified(fo.getLastModified());
			cfo.setMd5sum(fo.getMd5sum());
			cfo.setMimeType(fo.getMimeType());
			cfo.setCDNURL(fo.getCDNURL());
			cfo.setSize(fo.getSize());
		    
		} catch (Exception e) {
			logger.info("CloudFilesServiceImpl storeObject:: failed to store object, error: {}", e.getMessage());
			e.printStackTrace();
			throw new CloudFilesException(e.getMessage());
		}

		return cfo;
	}
	
	public List<CloudFilesObject> getObjects(String path) {
		List<CloudFilesObject> cfos = new ArrayList<CloudFilesObject>();
		try {
			List<FilesObject> fileObjects = filesClient.listObjectsStartingWith(rootContainer, path, path, 10, null);
			for (FilesObject fo : fileObjects) {
				CloudFilesObject cfo = new CloudFilesObject();
				cfo.setName(fo.getName());
				cfo.setLastModified(fo.getLastModified());
				cfo.setMd5sum(fo.getMd5sum());
				cfo.setMimeType(fo.getMimeType());
				cfo.setCDNURL(fo.getCDNURL());	
				cfo.setSize(fo.getSize());
				cfos.add(cfo);				
			}
		} catch (Exception e) {
			logger.info("CloudFilesServiceImpl getObjects:: failed to get objects, error: {}", e.getMessage());
			e.printStackTrace();
			throw new CloudFilesException(e.getMessage());
		}
			
		return cfos;
	}
	
	public CloudFilesObject deleteObject(String objectName) {
		CloudFilesObject cfo = new CloudFilesObject();
		cfo.setName(objectName);
		try {
			filesClient.deleteObject (rootContainer, objectName);
		} catch (Exception e) {
			logger.info("CloudFilesServiceImpl deleteObject:: failed to delete object, error: {}", e.getMessage());
			e.printStackTrace();
			throw new CloudFilesException(e.getMessage());
		}
		return cfo;
	}
	

}
