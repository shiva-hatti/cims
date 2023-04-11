package com.iris.dto;

import java.io.Serializable;

public class ApplicableCheckDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5118907452589255904L;

	private String entityCode;
	private String category;
	private String subCategory;
	private String returnCode;
	private Boolean customApplicable;
	private Boolean crossApplicable;

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public Boolean getCustomApplicable() {
		return customApplicable;
	}

	public void setCustomApplicable(Boolean customApplicable) {
		this.customApplicable = customApplicable;
	}

	public Boolean getCrossApplicable() {
		return crossApplicable;
	}

	public void setCrossApplicable(Boolean crossApplicable) {
		this.crossApplicable = crossApplicable;
	}

}
