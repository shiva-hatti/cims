package com.iris.dto;

import java.util.List;

public class WebTaxVersionInfoDto {

	private List<Long> returnSecMapIdsList;
	private String versionNo;
	private String fileName;
	private Integer returnTemplateId;

	public List<Long> getReturnSecMapIdsList() {
		return returnSecMapIdsList;
	}

	public void setReturnSecMapIdsList(List<Long> returnSecMapIdsList) {
		this.returnSecMapIdsList = returnSecMapIdsList;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getReturnTemplateId() {
		return returnTemplateId;
	}

	public void setReturnTemplateId(Integer returnTemplateId) {
		this.returnTemplateId = returnTemplateId;
	}

}
