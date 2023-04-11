package com.iris.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.iris.model.UserMaster;

public class ReturnGroupMappingDto implements Serializable {

	private static final long serialVersionUID = 5717195175755935032L;

	private Long returnGroupMapId;
	private String defaultGroupName;
	private Boolean isActive;
	private UserMaster createdBy;
	private Date createdOn;
	private Date lastModifiedOn;
	private Long lastModifiedBy;
	private List<ReturnDto> returnList;
	private Float orderNo;
	private int returnCount;
	private int singleLanguage;
	private Long roleIdKey;
	private Long createdOnInLong;
	private String allottedReturns;
	private Boolean isCrossValidation;

	@Override
	public int hashCode() {
		return returnGroupMapId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else {
			if (this.getClass() != obj.getClass()) {
				return false;
			} else {
				ReturnGroupMappingDto returnGroupMappingDto = (ReturnGroupMappingDto) obj;
				return returnGroupMappingDto.getReturnGroupMapId().equals((this.getReturnGroupMapId()));
			}
		}
	}

	/**
	 * @return the returnGroupMapId
	 */
	public Long getReturnGroupMapId() {
		return returnGroupMapId;
	}

	/**
	 * @param returnGroupMapId the returnGroupMapId to set
	 */
	public void setReturnGroupMapId(Long returnGroupMapId) {
		this.returnGroupMapId = returnGroupMapId;
	}

	/**
	 * @return the defaultGroupName
	 */
	public String getDefaultGroupName() {
		return defaultGroupName;
	}

	/**
	 * @param defaultGroupName the defaultGroupName to set
	 */
	public void setDefaultGroupName(String defaultGroupName) {
		this.defaultGroupName = defaultGroupName;
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
	 * @return the createdBy
	 */
	public UserMaster getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the lastModifiedOn
	 */
	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	/**
	 * @param lastModifiedOn the lastModifiedOn to set
	 */
	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public List<ReturnDto> getReturnList() {
		return returnList;
	}

	public void setReturnList(List<ReturnDto> returnList) {
		this.returnList = returnList;
	}

	/**
	 * @return the orderNo
	 */
	public Float getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(Float orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the returnCount
	 */
	public int getReturnCount() {
		return returnCount;
	}

	/**
	 * @param returnCount the returnCount to set
	 */
	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}

	/**
	 * @return the singleLanguage
	 */
	public int getSingleLanguage() {
		return singleLanguage;
	}

	/**
	 * @param singleLanguage the singleLanguage to set
	 */
	public void setSingleLanguage(int singleLanguage) {
		this.singleLanguage = singleLanguage;
	}

	/**
	 * @return the roleIdKey
	 */
	public Long getRoleIdKey() {
		return roleIdKey;
	}

	/**
	 * @param roleIdKey the roleIdKey to set
	 */
	public void setRoleIdKey(Long roleIdKey) {
		this.roleIdKey = roleIdKey;
	}

	/**
	 * @return the createdOnInLong
	 */
	public Long getCreatedOnInLong() {
		return createdOnInLong;
	}

	/**
	 * @param createdOnInLong the createdOnInLong to set
	 */
	public void setCreatedOnInLong(Long createdOnInLong) {
		this.createdOnInLong = createdOnInLong;
	}

	/**
	 * @return the allottedReturns
	 */
	public String getAllottedReturns() {
		return allottedReturns;
	}

	/**
	 * @param allottedReturns the allottedReturns to set
	 */
	public void setAllottedReturns(String allottedReturns) {
		this.allottedReturns = allottedReturns;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Boolean getIsCrossValidation() {
		return isCrossValidation;
	}

	public void setIsCrossValidation(Boolean isCrossValidation) {
		this.isCrossValidation = isCrossValidation;
	}

}