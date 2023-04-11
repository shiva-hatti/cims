/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author apagaria
 *
 */
public class ReturnEntityDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String entityCode;

	private String entityName;

	private String categoryCode;

	private String categoryName;

	private String subCategoryName;

	private String subCategoryCode;

	private Boolean uploadChannel;

	private Boolean webChannel;

	private Boolean emailChannel;

	private Boolean apiChannel;

	private Boolean stsChannel;

	/**
	 * @return the entityCode
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * @param entityCode the entityCode to set
	 */
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * @return the categoryCode
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * @param categoryCode the categoryCode to set
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return the subCategoryName
	 */
	public String getSubCategoryName() {
		return subCategoryName;
	}

	/**
	 * @param subCategoryName the subCategoryName to set
	 */
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	/**
	 * @return the subCategoryCode
	 */
	public String getSubCategoryCode() {
		return subCategoryCode;
	}

	/**
	 * @param subCategoryCode the subCategoryCode to set
	 */
	public void setSubCategoryCode(String subCategoryCode) {
		this.subCategoryCode = subCategoryCode;
	}

	/**
	 * @return the uploadChannel
	 */
	public Boolean getUploadChannel() {
		return uploadChannel;
	}

	/**
	 * @param uploadChannel the uploadChannel to set
	 */
	public void setUploadChannel(Boolean uploadChannel) {
		this.uploadChannel = uploadChannel;
	}

	/**
	 * @return the webChannel
	 */
	public Boolean getWebChannel() {
		return webChannel;
	}

	/**
	 * @param webChannel the webChannel to set
	 */
	public void setWebChannel(Boolean webChannel) {
		this.webChannel = webChannel;
	}

	/**
	 * @return the emailChannel
	 */
	public Boolean getEmailChannel() {
		return emailChannel;
	}

	/**
	 * @param emailChannel the emailChannel to set
	 */
	public void setEmailChannel(Boolean emailChannel) {
		this.emailChannel = emailChannel;
	}

	/**
	 * @return the apiChannel
	 */
	public Boolean getApiChannel() {
		return apiChannel;
	}

	/**
	 * @param apiChannel the apiChannel to set
	 */
	public void setApiChannel(Boolean apiChannel) {
		this.apiChannel = apiChannel;
	}

	/**
	 * @return the stsChannel
	 */
	public Boolean getStsChannel() {
		return stsChannel;
	}

	/**
	 * @param stsChannel the stsChannel to set
	 */
	public void setStsChannel(Boolean stsChannel) {
		this.stsChannel = stsChannel;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "ReturnEntityDto [entityCode=" + entityCode + ", entityName=" + entityName + ", subCategoryName=" + subCategoryName + ", subCategoryCode=" + subCategoryCode + ", uploadChannel=" + uploadChannel + ", webChannel=" + webChannel + ", emailChannel=" + emailChannel + ", apiChannel=" + apiChannel + ", stsChannel=" + stsChannel + "]";
	}
}
