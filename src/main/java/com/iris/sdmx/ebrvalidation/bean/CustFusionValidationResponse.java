package com.iris.sdmx.ebrvalidation.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CustFusionValidationResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8605577175657942537L;

	private String entityCode;

	private Long fileDetailsId;

	private Date uploadedOn;

	private boolean isValidationFailed;

	private List<CustFusionErrorType> errorTypes;

	public List<CustFusionErrorType> getErrorTypes() {
		return errorTypes;
	}

	public void setErrorTypes(List<CustFusionErrorType> errorTypes) {
		this.errorTypes = errorTypes;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public Long getFileDetailsId() {
		return fileDetailsId;
	}

	public void setFileDetailsId(Long fileDetailsId) {
		this.fileDetailsId = fileDetailsId;
	}

	public Date getUploadedOn() {
		return uploadedOn;
	}

	public void setUploadedOn(Date uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public boolean isValidationFailed() {
		return isValidationFailed;
	}

	public void setValidationFailed(boolean isValidationFailed) {
		this.isValidationFailed = isValidationFailed;
	}

}
