package com.internal.recipes.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.internal.recipes.domain.CloudFilesObject;

public interface CloudFilesService {
	public CloudFilesObject storeObject(MultipartFile file, String path, String filename);	
	public List<CloudFilesObject> getObjects(String path);
	public CloudFilesObject deleteObject(String objectName);
}
