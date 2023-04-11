package com.iris.dto;

import java.io.Serializable;
import com.iris.util.Validations;

public class XbrlReturnTempUploadDTO implements Serializable {

	private static final long serialVersionUID = -4779950187552319982L;
	private String xbrlVersionNumber;
	private String xbrlVersionDesc;
	private String xbrlValidFromDate;
	private String xbrlXSDFileName;
	private String xbrlTaxonomyFileName;
	private Long returnTypeId;
	private Boolean isActive;
	private String xbrlTaxonomyFileEncString;
	private String xbrlWebFormJSON;
	private String xbrlReturnFullPackageFileName;

	public String getXbrlVersionNumber() {
		return xbrlVersionNumber;
	}

	public void setXbrlVersionNumber(String xbrlVersionNumber) {
		this.xbrlVersionNumber = Validations.trimInput(xbrlVersionNumber);
	}

	public String getXbrlVersionDesc() {
		return xbrlVersionDesc;
	}

	public void setXbrlVersionDesc(String xbrlVersionDesc) {
		this.xbrlVersionDesc = Validations.trimInput(xbrlVersionDesc);
	}

	public String getXbrlValidFromDate() {
		return xbrlValidFromDate;
	}

	public void setXbrlValidFromDate(String xbrlValidFromDate) {
		this.xbrlValidFromDate = Validations.trimInput(xbrlValidFromDate);
	}

	public String getXbrlXSDFileName() {
		return xbrlXSDFileName;
	}

	public void setXbrlXSDFileName(String xbrlXSDFileName) {
		this.xbrlXSDFileName = Validations.trimInput(xbrlXSDFileName);
	}

	public String getXbrlTaxonomyFileName() {
		return xbrlTaxonomyFileName;
	}

	public void setXbrlTaxonomyFileName(String xbrlTaxonomyFileName) {
		this.xbrlTaxonomyFileName = Validations.trimInput(xbrlTaxonomyFileName);
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

	public String getXbrlTaxonomyFileEncString() {
		return xbrlTaxonomyFileEncString;
	}

	public void setXbrlTaxonomyFileEncString(String xbrlTaxonomyFileEncString) {
		this.xbrlTaxonomyFileEncString = Validations.trimInput(xbrlTaxonomyFileEncString);
	}

	public String getXbrlWebFormJSON() {
		return xbrlWebFormJSON;
	}

	public void setXbrlWebFormJSON(String xbrlWebFormJSON) {
		this.xbrlWebFormJSON = Validations.trimInput(xbrlWebFormJSON);
	}

	public String getXbrlReturnFullPackageFileName() {
		return xbrlReturnFullPackageFileName;
	}

	public void setXbrlReturnFullPackageFileName(String xbrlReturnFullPackageFileName) {
		this.xbrlReturnFullPackageFileName = Validations.trimInput(xbrlReturnFullPackageFileName);
	}

}