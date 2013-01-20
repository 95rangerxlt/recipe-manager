package com.internal.recipes.domain;

public class CloudFilesObject {
    private String name;
    private String md5sum = null;
    private long size = -1;
    private String mimeType = null;
    private String lastModified = null;
    private String CDNURL;
    
	public String getCDNURL() {
		return CDNURL;
	}

	public void setCDNURL(String CDNURL) {
		this.CDNURL = CDNURL;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getMd5sum() {
		return md5sum;
	}

	public void setMd5sum(String md5sum) {
		this.md5sum = md5sum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    
}
