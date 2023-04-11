package com.iris.sdmx.element.bean;

import java.util.Date;

public class SdmxElementRegulatorBean {

	private Long regulatorId;

	private String regulatorName;

	private String regulatorCode;

	private Boolean isActive;

	private Date lastUpdatedOn;

	private Date createdOn;

	private Date lastModifiedOn;

	private Long createdBy;

	private Long modifyBy;

	private Long userId;

	private Long roleId;

	private String langCode;

	public Long getRegulatorId() {
		return regulatorId;
	}

	public void setRegulatorId(Long regulatorId) {
		this.regulatorId = regulatorId;
	}

	public String getRegulatorName() {
		return regulatorName;
	}

	public void setRegulatorName(String regulatorName) {
		this.regulatorName = regulatorName;
	}

	public String getRegulatorCode() {
		return regulatorCode;
	}

	public void setRegulatorCode(String regulatorCode) {
		this.regulatorCode = regulatorCode;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(Long modifyBy) {
		this.modifyBy = modifyBy;
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

	/**
	 * @return the langCode
	 */
	public String getLangCode() {
		return langCode;
	}

	/**
	 * @param langCode the langCode to set
	 */
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	@Override
	public String toString() {
		return "SdmxElementRegulatorBean [regulatorId=" + regulatorId + ", regulatorName=" + regulatorName + ", regulatorCode=" + regulatorCode + ", isActive=" + isActive + ", lastUpdatedOn=" + lastUpdatedOn + ", createdOn=" + createdOn + ", lastModifiedOn=" + lastModifiedOn + ", createdBy=" + createdBy + ", modifyBy=" + modifyBy + "]";
	}

}
