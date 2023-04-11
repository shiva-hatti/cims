package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EntityFilingPendingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5670932534989582924L;
	private long userId;
	private long roleId;
	private long menuId;
	private String dateFormatString;
	private String langCode;
	private List<EntityFilingPendingData> entityFilingPendingDataList;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public long getMenuId() {
		return menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	public List<EntityFilingPendingData> getEntityFilingPendingDataList() {
		return entityFilingPendingDataList;
	}

	public void setEntityFilingPendingDataList(List<EntityFilingPendingData> entityFilingPendingDataList) {
		this.entityFilingPendingDataList = entityFilingPendingDataList;
	}

	public String getDateFormatString() {
		return dateFormatString;
	}

	public void setDateFormatString(String dateFormatString) {
		this.dateFormatString = dateFormatString;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

}
