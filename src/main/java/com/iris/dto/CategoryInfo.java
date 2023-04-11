package com.iris.dto;

import java.io.Serializable;

import com.iris.util.Validations;

public class CategoryInfo implements Serializable {

	private static final long serialVersionUID = -2944226563269523351L;

	private String categoryCode;
	private String[] subCatInfo;
	private String iDocId;
	private String genericIDocId;

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = Validations.trimInput(categoryCode);
	}

	public String[] getSubCatInfo() {
		return subCatInfo;
	}

	public void setSubCatInfo(String[] subCatInfo) {
		this.subCatInfo = subCatInfo;
	}

	public String getiDocId() {
		return iDocId;
	}

	public void setiDocId(String iDocId) {
		this.iDocId = Validations.trimInput(iDocId);
	}

	public String getGenericIDocId() {
		return genericIDocId;
	}

	public void setGenericIDocId(String genericIDocId) {
		this.genericIDocId = Validations.trimInput(genericIDocId);
	}

}