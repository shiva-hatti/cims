package com.iris.sdmx.dimesnsion.bean;

import java.io.Serializable;
import java.util.Date;

import com.iris.model.UserMaster;

/**
 * @author vjadhav
 *
 */
public class DimensionModBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7303090352707713748L;

	private Long dimMasterModId;

	private Long dimIdFk;

	private String dimCode;

	private String dimMasterJson;

	private Boolean isActive;

	private int adminStatusId;

	private int actionId;

	private String comments;

	private Long createdBy;

	private Date createdOn;

	private Long lastModifiedBy;

	private Date lastModifiedOn;

	private Date lastUpdatedOn;

	private Long lastApprovedBy;

	private Date lastApprovedOn;

	private String actionLabel;

	private String adminStatusLabel;

	private String createdByName;

	private Long agencyIdFk;

	private String agencyLabel;

	private String conceptVersion;

	private String agencyCode;

	/**
	 * @return the agencyCode
	 */
	public String getAgencyCode() {
		return agencyCode;
	}

	/**
	 * @param agencyCode the agencyCode to set
	 */
	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

	/**
	 * @return the dimMasterModId
	 */
	public Long getDimMasterModId() {
		return dimMasterModId;
	}

	/**
	 * @param dimMasterModId the dimMasterModId to set
	 */
	public void setDimMasterModId(Long dimMasterModId) {
		this.dimMasterModId = dimMasterModId;
	}

	/**
	 * @return the dimIdFk
	 */
	public Long getDimIdFk() {
		return dimIdFk;
	}

	/**
	 * @param dimIdFk the dimIdFk to set
	 */
	public void setDimIdFk(Long dimIdFk) {
		this.dimIdFk = dimIdFk;
	}

	/**
	 * @return the dimCode
	 */
	public String getDimCode() {
		return dimCode;
	}

	/**
	 * @param dimCode the dimCode to set
	 */
	public void setDimCode(String dimCode) {
		this.dimCode = dimCode;
	}

	/**
	 * @return the dimMasterJson
	 */
	public String getDimMasterJson() {
		return dimMasterJson;
	}

	/**
	 * @param dimMasterJson the dimMasterJson to set
	 */
	public void setDimMasterJson(String dimMasterJson) {
		this.dimMasterJson = dimMasterJson;
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
	 * @return the adminStatusId
	 */
	public int getAdminStatusId() {
		return adminStatusId;
	}

	/**
	 * @param adminStatusId the adminStatusId to set
	 */
	public void setAdminStatusId(int adminStatusId) {
		this.adminStatusId = adminStatusId;
	}

	/**
	 * @return the actionId
	 */
	public int getActionId() {
		return actionId;
	}

	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
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

	/**
	 * @return the lastUpdatedOn
	 */
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	/**
	 * @param lastUpdatedOn the lastUpdatedOn to set
	 */
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	/**
	 * @return the lastApprovedBy
	 */
	public Long getLastApprovedBy() {
		return lastApprovedBy;
	}

	/**
	 * @param lastApprovedBy the lastApprovedBy to set
	 */
	public void setLastApprovedBy(Long lastApprovedBy) {
		this.lastApprovedBy = lastApprovedBy;
	}

	/**
	 * @return the lastApprovedOn
	 */
	public Date getLastApprovedOn() {
		return lastApprovedOn;
	}

	/**
	 * @param lastApprovedOn the lastApprovedOn to set
	 */
	public void setLastApprovedOn(Date lastApprovedOn) {
		this.lastApprovedOn = lastApprovedOn;
	}

	/**
	 * @return the actionLabel
	 */
	public String getActionLabel() {
		return actionLabel;
	}

	/**
	 * @param actionLabel the actionLabel to set
	 */
	public void setActionLabel(String actionLabel) {
		this.actionLabel = actionLabel;
	}

	/**
	 * @return the adminStatusLabel
	 */
	public String getAdminStatusLabel() {
		return adminStatusLabel;
	}

	/**
	 * @param adminStatusLabel the adminStatusLabel to set
	 */
	public void setAdminStatusLabel(String adminStatusLabel) {
		this.adminStatusLabel = adminStatusLabel;
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
	 * @return the agencyIdFk
	 */
	public Long getAgencyIdFk() {
		return agencyIdFk;
	}

	/**
	 * @param agencyIdFk the agencyIdFk to set
	 */
	public void setAgencyIdFk(Long agencyIdFk) {
		this.agencyIdFk = agencyIdFk;
	}

	/**
	 * @return the agencyLabel
	 */
	public String getAgencyLabel() {
		return agencyLabel;
	}

	/**
	 * @param agencyLabel the agencyLabel to set
	 */
	public void setAgencyLabel(String agencyLabel) {
		this.agencyLabel = agencyLabel;
	}

	/**
	 * @return the conceptVersion
	 */
	public String getConceptVersion() {
		return conceptVersion;
	}

	/**
	 * @param conceptVersion the conceptVersion to set
	 */
	public void setConceptVersion(String conceptVersion) {
		this.conceptVersion = conceptVersion;
	}

}
