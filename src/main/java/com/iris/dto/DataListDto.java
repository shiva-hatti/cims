/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.model.Return;

/**
 * @author sajadhav
 *
 */
@JsonInclude(Include.NON_DEFAULT)
public class DataListDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4590915928428934621L;
	private Long userId;
	private List<Long> returnIdList;
	private List<Long> deActiveReturnIdList;
	private List<String> returnCodeList;
	private List<String> deActiveReturnCodeList;
	private List<Long> entityIdList;
	private List<Long> subCatIdList;
	private List<Return> returnsList;
	private Long entityId;
	private Long cateId;
	private Long subCatId;
	private Boolean isGroupMapp;
	private String langCode;
	private String dateFormat;
	private Long roleId;
	private Long alertId;
	private Long menuId;
	private String calendarFormat;
	private Long returnId;
	private String startDate;
	private String endDate;
	private List<Long> statusIdList;
	private Long langId;
	private List<String> subCatCodeList;
	private List<String> catCodeList;
	private String entityCode;
	private List<ReturnChannelDetailsDto> channelDetails;

	/**
	 * @return the channelDetails
	 */
	public List<ReturnChannelDetailsDto> getChannelDetails() {
		return channelDetails;
	}

	/**
	 * @param channelDetails the channelDetails to set
	 */
	public void setChannelDetails(List<ReturnChannelDetailsDto> channelDetails) {
		this.channelDetails = channelDetails;
	}

	/**
	 * @return the returnId
	 */
	public Long getReturnId() {
		return returnId;
	}

	/**
	 * @param returnId the returnId to set
	 */
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public String getCalendarFormat() {
		return calendarFormat;
	}

	public void setCalendarFormat(String calendarFormat) {
		this.calendarFormat = calendarFormat;
	}

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @return the returnsList
	 */
	public List<Return> getReturnsList() {
		return returnsList;
	}

	/**
	 * @param returnsList the returnsList to set
	 */
	public void setReturnsList(List<Return> returnsList) {
		this.returnsList = returnsList;
	}

	public Boolean getIsGroupMapp() {
		return isGroupMapp;
	}

	public void setIsGroupMapp(Boolean isGroupMapp) {
		this.isGroupMapp = isGroupMapp;
	}

	public List<Long> getDeActiveReturnIdList() {
		return deActiveReturnIdList;
	}

	public void setDeActiveReturnIdList(List<Long> deActiveReturnIdList) {
		this.deActiveReturnIdList = deActiveReturnIdList;
	}

	//	public List<Return> getRetObjList() {
	//		return retObjList;
	//	}
	//
	//
	//	public void setRetObjList(List<Return> retObjList) {
	//		this.retObjList = retObjList;
	//	}

	public Long getCateId() {
		return cateId;
	}

	public void setCateId(Long cateId) {
		this.cateId = cateId;
	}

	public Long getSubCatId() {
		return subCatId;
	}

	public void setSubCatId(Long subCatId) {
		this.subCatId = subCatId;
	}

	public Long getUserId() {
		return userId;
	}

	public List<Long> getReturnIdList() {
		return returnIdList;
	}

	public void setReturnIdList(List<Long> returnIdList) {
		this.returnIdList = returnIdList;
	}

	public List<Long> getEntityIdList() {
		return entityIdList;
	}

	public void setEntityIdList(List<Long> entityIdList) {
		this.entityIdList = entityIdList;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public List<Long> getStatusIdList() {
		return statusIdList;
	}

	public void setStatusIdList(List<Long> statusIdList) {
		this.statusIdList = statusIdList;
	}

	public Long getLangId() {
		return langId;
	}

	public void setLangId(Long langId) {
		this.langId = langId;
	}

	public List<Long> getSubCatIdList() {
		return subCatIdList;
	}

	public void setSubCatIdList(List<Long> subCatIdList) {
		this.subCatIdList = subCatIdList;
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

	public List<String> getCatCodeList() {
		return catCodeList;
	}

	public void setCatCodeList(List<String> catCodeList) {
		this.catCodeList = catCodeList;
	}

	/**
	 * @return the returnCodeList
	 */
	public List<String> getReturnCodeList() {
		return returnCodeList;
	}

	/**
	 * @param returnCodeList the returnCodeList to set
	 */
	public void setReturnCodeList(List<String> returnCodeList) {
		this.returnCodeList = returnCodeList;
	}

	/**
	 * @return the deActiveReturnCodeList
	 */
	public List<String> getDeActiveReturnCodeList() {
		return deActiveReturnCodeList;
	}

	/**
	 * @param deActiveReturnCodeList the deActiveReturnCodeList to set
	 */
	public void setDeActiveReturnCodeList(List<String> deActiveReturnCodeList) {
		this.deActiveReturnCodeList = deActiveReturnCodeList;
	}

}
