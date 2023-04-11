package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.iris.util.Validations;

public class SapBoDto implements Serializable {

	private static final long serialVersionUID = -6977805432322122494L;

	private String returnCode;
	private String genericIDocId;
	private List<CategoryInfo> categoryInfo;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = Validations.trimInput(returnCode);
	}

	public String getGenericIDocId() {
		return genericIDocId;
	}

	public void setGenericIDocId(String genericIDocId) {
		this.genericIDocId = Validations.trimInput(genericIDocId);
	}

	public List<CategoryInfo> getCategoryInfo() {
		return categoryInfo;
	}

	public void setCategoryInfo(List<CategoryInfo> categoryInfo) {
		this.categoryInfo = categoryInfo;
	}

}