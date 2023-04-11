package com.iris.sdmx.agency.master.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.iris.model.UserMaster;

@Entity
@Table(name = "TBL_SDMX_AGENCY_MASTER")
public class AgencyMaster implements Serializable {

	/**
	 * @author sdhone
	 */
	private static final long serialVersionUID = -8629283887008035358L;

	@Id
	@Column(name = "SDMX_AGENCY_MASTER_ID")
	private Long agencyMasterId;

	@Column(name = "AGENCY_MASTER_CODE")
	private String agencyMasterCode;

	@Column(name = "AGENCY_MASTER_LABEL")
	private String agencyMasterLabel;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "AGENCY_FUSION_NAME")
	private String agencyFusionName;

	/**
	 * @param agencyMasterId
	 */
	public AgencyMaster(Long agencyMasterId) {
		this.agencyMasterId = agencyMasterId;
	}

	/**
	 * 
	 */
	public AgencyMaster() {
	}

	/**
	 * @return the agencyMasterId
	 */
	public Long getAgencyMasterId() {
		return agencyMasterId;
	}

	/**
	 * @param agencyMasterId the agencyMasterId to set
	 */
	public void setAgencyMasterId(Long agencyMasterId) {
		this.agencyMasterId = agencyMasterId;
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
	 * @return the agencyMasterLabel
	 */
	public String getAgencyMasterLabel() {
		return agencyMasterLabel;
	}

	/**
	 * @param agencyMasterLabel the agencyMasterLabel to set
	 */
	public void setAgencyMasterLabel(String agencyMasterLabel) {
		this.agencyMasterLabel = agencyMasterLabel;
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
	 * @return the agencyFusionName
	 */
	public String getAgencyFusionName() {
		return agencyFusionName;
	}

	/**
	 * @param agencyFusionName the agencyFusionName to set
	 */
	public void setAgencyFusionName(String agencyFusionName) {
		this.agencyFusionName = agencyFusionName;
	}

}
