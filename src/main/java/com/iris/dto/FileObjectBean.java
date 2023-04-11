package com.iris.dto;

import java.io.Serializable;

import com.iris.util.Validations;

public class FileObjectBean implements Serializable {

	private static final long serialVersionUID = 6005069988274996224L;
	private String fileType;
	private Long fileSize;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = Validations.trimInput(fileType);
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

}