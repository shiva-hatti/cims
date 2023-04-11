/**
 * 
 */
package com.iris.sdmx.element.bean;

import java.util.Date;

/**
 * @author apagaria
 *
 */
public class SdmxElementTempBean {

	private Long elementTempId;

	private String dsdCode;

	private SdmxElementBeanForTemp sdmxElementEntity;

	private String elementVer;

	private Long createdBy;

	private String createdByName;

	private Date createdOn;

	private Long statusId;

	private String statusLabel;

	private Long actionStatusId;

	private String actionStatusLabel;

	private Long elementIdFk;

	private Long createdOnLong;

	private String sdmxElementEntityJson;

	private String agencyMasterCode;

	/**
	 * @return the elementTempId
	 */
	public Long getElementTempId() {
		return elementTempId;
	}

	/**
	 * @param elementTempId the elementTempId to set
	 */
	public void setElementTempId(Long elementTempId) {
		this.elementTempId = elementTempId;
	}

	/**
	 * @return the dsdCode
	 */
	public String getDsdCode() {
		return dsdCode;
	}

	/**
	 * @param dsdCode the dsdCode to set
	 */
	public void setDsdCode(String dsdCode) {
		this.dsdCode = dsdCode;
	}

	/**
	 * @return the sdmxElementEntity
	 */
	public SdmxElementBeanForTemp getSdmxElementEntity() {
		return sdmxElementEntity;
	}

	/**
	 * @param sdmxElementEntity the sdmxElementEntity to set
	 */
	public void setSdmxElementEntity(SdmxElementBeanForTemp sdmxElementEntity) {
		this.sdmxElementEntity = sdmxElementEntity;
	}

	/**
	 * @return the elementVer
	 */
	public String getElementVer() {
		return elementVer;
	}

	/**
	 * @param elementVer the elementVer to set
	 */
	public void setElementVer(String elementVer) {
		this.elementVer = elementVer;
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
	 * @return the statusId
	 */
	public Long getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return the actionStatusId
	 */
	public Long getActionStatusId() {
		return actionStatusId;
	}

	/**
	 * @param actionStatusId the actionStatusId to set
	 */
	public void setActionStatusId(Long actionStatusId) {
		this.actionStatusId = actionStatusId;
	}

	/**
	 * @return the elementIdFk
	 */
	public Long getElementIdFk() {
		return elementIdFk;
	}

	/**
	 * @param elementIdFk the elementIdFk to set
	 */
	public void setElementIdFk(Long elementIdFk) {
		this.elementIdFk = elementIdFk;
	}

	/**
	 * @return the createdOnLong
	 */
	public Long getCreatedOnLong() {
		return createdOnLong;
	}

	/**
	 * @param createdOnLong the createdOnLong to set
	 */
	public void setCreatedOnLong(Long createdOnLong) {
		this.createdOnLong = createdOnLong;
	}

	/**
	 * @return the sdmxElementEntityJson
	 */
	public String getSdmxElementEntityJson() {
		return sdmxElementEntityJson;
	}

	/**
	 * @param sdmxElementEntityJson the sdmxElementEntityJson to set
	 */
	public void setSdmxElementEntityJson(String sdmxElementEntityJson) {
		this.sdmxElementEntityJson = sdmxElementEntityJson;
	}

	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}

	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	/**
	 * @return the statusLabel
	 */
	public String getStatusLabel() {
		return statusLabel;
	}

	/**
	 * @param statusLabel the statusLabel to set
	 */
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	/**
	 * @return the actionStatusLabel
	 */
	public String getActionStatusLabel() {
		return actionStatusLabel;
	}

	/**
	 * @param actionStatusLabel the actionStatusLabel to set
	 */
	public void setActionStatusLabel(String actionStatusLabel) {
		this.actionStatusLabel = actionStatusLabel;
	}

	/**
	 * @return the agencyMasterCode
	 */
	public String getAgencyMasterCode() {
		return agencyMasterCode;
	}

	/**
	 * @param agencyMasterCode the agencyMasterCode to set
	 */
	public void setAgencyMasterCode(String agencyMasterCode) {
		this.agencyMasterCode = agencyMasterCode;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "SdmxElementTempBean [elementTempId=" + elementTempId + ", dsdCode=" + dsdCode + ", sdmxElementEntity=" + sdmxElementEntity + ", elementVer=" + elementVer + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", statusId=" + statusId + ", actionStatusId=" + actionStatusId + "]";
	}

}
