/**
 * 
 */
package com.iris.sdmx.element.bean;

import java.util.Date;

/**
 * @author apagaria
 *
 */
public class SdmxElementDependencyTypeBean {

	private Long dependencyTypeId;

	private String dependencyTypeName;

	private Boolean isActive;

	private Date lastUpdateOn;

	private Long createdBy;

	private Date createdOn;

	private Long modifyBy;

	private Date modifyOn;

	private Long userId;

	private Long roleId;

	private String langCode;

	/**
	 * @return the dependencyTypeId
	 */
	public Long getDependencyTypeId() {
		return dependencyTypeId;
	}

	/**
	 * @param dependencyTypeId the dependencyTypeId to set
	 */
	public void setDependencyTypeId(Long dependencyTypeId) {
		this.dependencyTypeId = dependencyTypeId;
	}

	/**
	 * @return the dependencyTypeName
	 */
	public String getDependencyTypeName() {
		return dependencyTypeName;
	}

	/**
	 * @param dependencyTypeName the dependencyTypeName to set
	 */
	public void setDependencyTypeName(String dependencyTypeName) {
		this.dependencyTypeName = dependencyTypeName;
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
	 * @return the lastUpdateOn
	 */
	public Date getLastUpdateOn() {
		return lastUpdateOn;
	}

	/**
	 * @param lastUpdateOn the lastUpdateOn to set
	 */
	public void setLastUpdateOn(Date lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}

	/**
	 * @return the createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Long createdBy) {
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
	 * @return the modifyBy
	 */
	public Long getModifyBy() {
		return modifyBy;
	}

	/**
	 * @param modifyBy the modifyBy to set
	 */
	public void setModifyBy(Long modifyBy) {
		this.modifyBy = modifyBy;
	}

	/**
	 * @return the modifyOn
	 */
	public Date getModifyOn() {
		return modifyOn;
	}

	/**
	 * @param modifyOn the modifyOn to set
	 */
	public void setModifyOn(Date modifyOn) {
		this.modifyOn = modifyOn;
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

	/**
	 *
	 */
	@Override
	public String toString() {
		return "SdmxElementDependencyTypeBean [dependencyTypeId=" + dependencyTypeId + ", dependencyTypeName=" + dependencyTypeName + ", isActive=" + isActive + ", lastUpdateOn=" + lastUpdateOn + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifyBy=" + modifyBy + ", modifyOn=" + modifyOn + "]";
	}
}
