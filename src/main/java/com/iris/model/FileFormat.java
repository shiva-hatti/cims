package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_FILE_FORMAT")
public class FileFormat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FILE_FORMAT_ID")
	private Long fileFormatId;

	@Column(name = "FORMAT_DESC")
	private String formatDesc;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	public Long getFileFormatId() {
		return fileFormatId;
	}

	public void setFileFormatId(Long fileFormatId) {
		this.fileFormatId = fileFormatId;
	}

	public String getFormatDesc() {
		return formatDesc;
	}

	public void setFormatDesc(String formatDesc) {
		this.formatDesc = formatDesc;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
