package com.iris.dto;

import java.io.Serializable;

import com.iris.util.Validations;

/**
 * @author psawant
 * @version 1.0
 * @date 06/10/2020
 */
public class ErrorCodeMessageBean implements Serializable {

	private static final long serialVersionUID = -451056421237274269L;

	private String technicalErrorCode;
	private String businessErrorCode;
	private String errorDiscription;

	public ErrorCodeMessageBean() {

	}

	public ErrorCodeMessageBean(String technicalErrorCode, String businessErrorCode, String errorDiscription) {
		this.technicalErrorCode = technicalErrorCode;
		this.businessErrorCode = businessErrorCode;
		this.errorDiscription = errorDiscription;
	}

	public String getTechnicalErrorCode() {
		return technicalErrorCode;
	}

	public void setTechnicalErrorCode(String technicalErrorCode) {
		this.technicalErrorCode = Validations.trimInput(technicalErrorCode);
	}

	public String getBusinessErrorCode() {
		return businessErrorCode;
	}

	public void setBusinessErrorCode(String businessErrorCode) {
		this.businessErrorCode = Validations.trimInput(businessErrorCode);
	}

	public String getErrorDiscription() {
		return errorDiscription;
	}

	public void setErrorDiscription(String errorDiscription) {
		this.errorDiscription = Validations.trimInput(errorDiscription);
	}

}