/**
 * 
 */
package com.iris.sdmx.lockrecord.bean;

import java.io.Serializable;

/**
 * @author apagaria
 *
 */
public class SdmxLockRecordSetBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long lockedBy;

	private Long moduleId;

	private String recordDetailEncodedJson;

	private Long recordLockPeriod;

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
	 * @return the recordDetailEncodedJson
	 */
	public String getRecordDetailEncodedJson() {
		return recordDetailEncodedJson;
	}

	/**
	 * @param recordDetailEncodedJson the recordDetailEncodedJson to set
	 */
	public void setRecordDetailEncodedJson(String recordDetailEncodedJson) {
		this.recordDetailEncodedJson = recordDetailEncodedJson;
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
	 *
	 */
	@Override
	public String toString() {
		return "SdmxLockRecordSetBean [lockedBy=" + lockedBy + ", moduleId=" + moduleId + ", recordDetailEncodedJson=" + recordDetailEncodedJson + ", recordLockPeriod=" + recordLockPeriod + "]";
	}
}
