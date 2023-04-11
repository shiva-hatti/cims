package com.iris.dto;

import java.io.Serializable;

import com.iris.util.Validations;

public class SdmxReturnTempUploadDTO implements Serializable {

	private static final long serialVersionUID = 5267300179047443945L;

	private String sdmxVersionNumber;
	private String sdmxVersionDesc;
	private String sdmxValidFromDate;
	private String sdmxXSDFileName;
	private String sdmxTaxonomyFileName;
	private Long returnTypeId;
	private Boolean isActive;

	public String getSdmxVersionNumber() {
		return sdmxVersionNumber;
	}

	public void setSdmxVersionNumber(String sdmxVersionNumber) {
		this.sdmxVersionNumber = Validations.trimInput(sdmxVersionNumber);
	}

	public String getSdmxVersionDesc() {
		return sdmxVersionDesc;
	}

	public void setSdmxVersionDesc(String sdmxVersionDesc) {
		this.sdmxVersionDesc = Validations.trimInput(sdmxVersionDesc);
	}

	public String getSdmxValidFromDate() {
		return sdmxValidFromDate;
	}

	public void setSdmxValidFromDate(String sdmxValidFromDate) {
		this.sdmxValidFromDate = Validations.trimInput(sdmxValidFromDate);
	}

	public String getSdmxXSDFileName() {
		return sdmxXSDFileName;
	}

	public void setSdmxXSDFileName(String sdmxXSDFileName) {
		this.sdmxXSDFileName = Validations.trimInput(sdmxXSDFileName);
	}

	public String getSdmxTaxonomyFileName() {
		return sdmxTaxonomyFileName;
	}

	public void setSdmxTaxonomyFileName(String sdmxTaxonomyFileName) {
		this.sdmxTaxonomyFileName = Validations.trimInput(sdmxTaxonomyFileName);
	}

	public Long getReturnTypeId() {
		return returnTypeId;
	}

	public void setReturnTypeId(Long returnTypeId) {
		this.returnTypeId = returnTypeId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
