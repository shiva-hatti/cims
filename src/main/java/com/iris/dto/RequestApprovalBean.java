package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Pradnya Mhatre
 */
public class RequestApprovalBean implements Serializable {
	private static final long serialVersionUID = -2627074095618205268L;
	private String userName;
	private Long roleId;
	private Long userId;
	private Boolean isActive;
	private String langCode;
	private Long langId;
	private String[] selectedSubCategory;
	private String dateFormat;
	private String startDate;
	private String endDate;
	private Boolean isCount;
	private Long entityId;
	private List<Long> returnIdList;
	private Long startDateLong;
	private Long endDateLong;
	private String categoryId;
	private String subCategoryId;
	private List<Long> entityIdList;
	private List<Long> subCatIdList;
	private List<String> catCodeList;
	private List<String> subCatCodeList;
	private String entityCode;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public Long getLangId() {
		return langId;
	}

	public void setLangId(Long langId) {
		this.langId = langId;
	}

	public String[] getSelectedSubCategory() {
		return selectedSubCategory;
	}

	public void setSelectedSubCategory(String[] selectedSubCategory) {
		this.selectedSubCategory = selectedSubCategory;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsCount() {
		return isCount;
	}

	public void setIsCount(boolean isCount) {
		this.isCount = isCount;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public List<Long> getReturnIdList() {
		return returnIdList;
	}

	public void setReturnIdList(List<Long> returnIdList) {
		this.returnIdList = returnIdList;
	}

	public Long getStartDateLong() {
		return startDateLong;
	}

	public void setStartDateLong(Long startDateLong) {
		this.startDateLong = startDateLong;
	}

	public Long getEndDateLong() {
		return endDateLong;
	}

	public void setEndDateLong(Long endDateLong) {
		this.endDateLong = endDateLong;
	}

	public void setIsCount(Boolean isCount) {
		this.isCount = isCount;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(String subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public List<Long> getEntityIdList() {
		return entityIdList;
	}

	public void setEntityIdList(List<Long> entityIdList) {
		this.entityIdList = entityIdList;
	}

	public List<Long> getSubCatIdList() {
		return subCatIdList;
	}

	public void setSubCatIdList(List<Long> subCatIdList) {
		this.subCatIdList = subCatIdList;
	}

	public List<String> getCatCodeList() {
		return catCodeList;
	}

	public void setCatCodeList(List<String> catCodeList) {
		this.catCodeList = catCodeList;
	}

	public List<String> getSubCatCodeList() {
		return subCatCodeList;
	}

	public void setSubCatCodeList(List<String> subCatCodeList) {
		this.subCatCodeList = subCatCodeList;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

}
