/**
 * 
 */
package com.iris.sdmx.lockrecord.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author apagaria
 *
 */
public class SdmxLockRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Long lockRecordId;

	private Long lockedBy;

	private String lockedByUserName;

	private Long moduleId;

	private String recordDetailJson;

	private String recordDetailEncode;

	private Long recordLockPeriod;

	private Boolean isActive;

	private Long createdBy;

	private String createdByUserName;

	private Date createdOn;

	private Long releasedBy;

	private String releasedByUserName;

	private Date releasedOn;

	/**
	 * @return the lockRecordId
	 */
	public Long getLockRecordId() {
		return lockRecordId;
	}

	/**
	 * @param lockRecordId the lockRecordId to set
	 */
	public void setLockRecordId(Long lockRecordId) {
		this.lockRecordId = lockRecordId;
	}

	/**
	 * @return the lockedBy
	 */
	public Long getLockedBy() {
		return lockedBy;
	}

	/**
	 * @param lockedBy the lockedBy to set
	 */
	public void setLockedBy(Long lockedBy) {
		this.lockedBy = lockedBy;
	}

	/**
	 * @return the lockedByUserName
	 */
	public String getLockedByUserName() {
		return lockedByUserName;
	}

	/**
	 * @param lockedByUserName the lockedByUserName to set
	 */
	public void setLockedByUserName(String lockedByUserName) {
		this.lockedByUserName = lockedByUserName;
	}

	/**
	 * @return the moduleId
	 */
	public Long getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the recordDetailJson
	 */
	public String getRecordDetailJson() {
		return recordDetailJson;
	}

	/**
	 * @param recordDetailJson the recordDetailJson to set
	 */
	public void setRecordDetailJson(String recordDetailJson) {
		this.recordDetailJson = recordDetailJson;
	}

	/**
	 * @return the recordDetailEncode
	 */
	public String getRecordDetailEncode() {
		return recordDetailEncode;
	}

	/**
	 * @param recordDetailEncode the recordDetailEncode to set
	 */
	public void setRecordDetailEncode(String recordDetailEncode) {
		this.recordDetailEncode = recordDetailEncode;
	}

	/**
	 * @return the recordLockPeriod
	 */
	public Long getRecordLockPeriod() {
		return recordLockPeriod;
	}

	/**
	 * @param recordLockPeriod the recordLockPeriod to set
	 */
	public void setRecordLockPeriod(Long recordLockPeriod) {
		this.recordLockPeriod = recordLockPeriod;
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
	 * @return the createdByUserName
	 */
	public String getCreatedByUserName() {
		return createdByUserName;
	}

	/**
	 * @param createdByUserName the createdByUserName to set
	 */
	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = createdByUserName;
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
	 * @return the releasedBy
	 */
	public Long getReleasedBy() {
		return releasedBy;
	}

	/**
	 * @param releasedBy the releasedBy to set
	 */
	public void setReleasedBy(Long releasedBy) {
		this.releasedBy = releasedBy;
	}

	/**
	 * @return the releasedByUserName
	 */
	public String getReleasedByUserName() {
		return releasedByUserName;
	}

	/**
	 * @param releasedByUserName the releasedByUserName to set
	 */
	public void setReleasedByUserName(String releasedByUserName) {
		this.releasedByUserName = releasedByUserName;
	}

	/**
	 * @return the releasedOn
	 */
	public Date getReleasedOn() {
		return releasedOn;
	}

	/**
	 * @param releasedOn the releasedOn to set
	 */
	public void setReleasedOn(Date releasedOn) {
		this.releasedOn = releasedOn;
	}

	@Override
	public String toString() {
		return "SdmxLockRecordBean [lockRecordId=" + lockRecordId + ", lockedBy=" + lockedBy + ", lockedByUserName=" + lockedByUserName + ", moduleId=" + moduleId + ", recordDetailJson=" + recordDetailJson + ", recordDetailEncode=" + recordDetailEncode + ", recordLockPeriod=" + recordLockPeriod + ", isActive=" + isActive + ", createdBy=" + createdBy + ", createdByUserName=" + createdByUserName + ", createdOn=" + createdOn + ", releasedBy=" + releasedBy + ", releasedByUserName=" + releasedByUserName + ", releasedOn=" + releasedOn + "]";
	}

}
