/**
 * 
 */
package com.iris.dto;

import java.util.List;

/**
 * @author Svishwakarma
 *
 */
public class ViewHistoricalDto {

	private String categoryCode;
	private String subCategoryCode;
	private String languageCode;
	private boolean isActive;
	private Long userId;
	private Long roleId;
	private List<String> entCodeList;
	private boolean isCategoryWiseResponse;
	private String entityNameLike;
	private List<String> subCategoryCodeList;
	private Long page;
	private Long fetchSize;
	private Long returnId;
	private String returnCode;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public List<String> getSubCategoryCodeList() {
		return subCategoryCodeList;
	}

	public void setSubCategoryCodeList(List<String> subCategoryCodeList) {
		this.subCategoryCodeList = subCategoryCodeList;
	}

	/**
	 * @return the entityNameLike
	 */
	public String getEntityNameLike() {
		return entityNameLike;
	}

	/**
	 * @param entityNameLike the entityNameLike to set
	 */
	public void setEntityNameLike(String entityNameLike) {
		this.entityNameLike = entityNameLike;
	}

	/**
	 * @return the isCategoryWiseResponse
	 */
	public boolean getIsCategoryWiseResponse() {
		return isCategoryWiseResponse;
	}

	/**
	 * @param isCategoryWiseResponse the isCategoryWiseResponse to set
	 */
	public void setIsCategoryWiseResponse(boolean isCategoryWiseResponse) {
		this.isCategoryWiseResponse = isCategoryWiseResponse;
	}

	public List<String> getEntCodeList() {
		return entCodeList;
	}

	public void setEntCodeList(List<String> entCodeList) {
		this.entCodeList = entCodeList;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the isActive
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
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
	 * @return the languageCode
	 */
	public String getLanguageCode() {
		return languageCode;
	}

	/**
	 * @param languageCode the languageCode to set
	 */
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public Long getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(Long fetchSize) {
		this.fetchSize = fetchSize;
	}

	public Long getReturnId() {
		return returnId;
	}

	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

}
