package com.internal.recipes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.internal.recipes.domain.CloudFilesObject;
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
	
	private FilesClient filesClient;
	
	@Autowired
	public void CloudFilesService()  {
		logger.info("CloudFilesServiceImpl ctor:: connecting to CloudFiles");
		filesClient = new FilesClient(apiUsername, apiKey, authUrl, "", connectionTimeout);
		try {
			filesClient.login();
		} catch (Exception e) {
			logger.info("CloudFilesServiceImpl ctor:: failed to make a connection to CloudFiles, error: {}", e.getMessage());
		}
	}


	public String storeObject(MultipartFile file, String path, String filename) {	   
		try {
		    filesClient.storeObject(rootContainer, file.getBytes(), file.getContentType(), path + "/" + filename, new HashMap<String,String>());
		} catch (Exception e) {
			logger.info("CloudFilesServiceImpl storeObject:: failed to store object, error: {}", e.getMessage());
			e.printStackTrace();
		}

		return null;
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
				cfo.setGetCDNURL(fo.getCDNURL());				
				cfos.add(cfo);				
			}
		} catch (Exception e) {
			logger.info("CloudFilesServiceImpl getObjects:: failed to get objects, error: {}", e.getMessage());
			e.printStackTrace();
		}
			
		return cfos;
	}

}
