package com.iris.dynamicDropDown.model;

import java.io.Serializable;

public class DropDownTypeDto implements Serializable {
	private static final long serialVersionUID = 6532738437725520190L;

	private Long dropdownTypeId;
	private String dropdownTypeCode;
	private String dropdownTypeName;
	private String returnCode;
	private Boolean isActive;
	private Boolean isFormAPI;
	private Boolean isConcept;
	private Boolean isDropdownVal;
	private String apiUrlDetailsJson;

	/**
	 * @return the dropdownTypeId
	 */
	public Long getDropdownTypeId() {
		return dropdownTypeId;
	}

	/**
	 * @param dropdownTypeId the dropdownTypeId to set
	 */
	public void setDropdownTypeId(Long dropdownTypeId) {
		this.dropdownTypeId = dropdownTypeId;
	}

	/**
	 * @return the dropdownTypeCode
	 */
	public String getDropdownTypeCode() {
		return dropdownTypeCode;
	}

	/**
	 * @param dropdownTypeCode the dropdownTypeCode to set
	 */
	public void setDropdownTypeCode(String dropdownTypeCode) {
		this.dropdownTypeCode = dropdownTypeCode;
	}

	/**
	 * @return the dropdownTypeName
	 */
	public String getDropdownTypeName() {
		return dropdownTypeName;
	}

	/**
	 * @param dropdownTypeName the dropdownTypeName to set
	 */
	public void setDropdownTypeName(String dropdownTypeName) {
		this.dropdownTypeName = dropdownTypeName;
	}

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the apiUrlDetailsJson
	 */
	public String getApiUrlDetailsJson() {
		return apiUrlDetailsJson;
	}

	/**
	 * @param apiUrlDetailsJson the apiUrlDetailsJson to set
	 */
	public void setApiUrlDetailsJson(String apiUrlDetailsJson) {
		this.apiUrlDetailsJson = apiUrlDetailsJson;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the isFormAPI
	 */
	public Boolean getIsFormAPI() {
		return isFormAPI;
	}

	/**
	 * @param isFormAPI the isFormAPI to set
	 */
	public void setIsFormAPI(Boolean isFormAPI) {
		this.isFormAPI = isFormAPI;
	}

	/**
	 * @return the isConcept
	 */
	public Boolean getIsConcept() {
		return isConcept;
	}

	/**
	 * @param isConcept the isConcept to set
	 */
	public void setIsConcept(Boolean isConcept) {
		this.isConcept = isConcept;
	}

	/**
	 * @return the isDropdownVal
	 */
	public Boolean getIsDropdownVal() {
		return isDropdownVal;
	}

	/**
	 * @param isDropdownVal the isDropdownVal to set
	 */
	public void setIsDropdownVal(Boolean isDropdownVal) {
		this.isDropdownVal = isDropdownVal;
	}
}
