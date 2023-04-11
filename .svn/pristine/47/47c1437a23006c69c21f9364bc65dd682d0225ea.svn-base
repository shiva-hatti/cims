/**
 * 
 */
package com.iris.sdmx.dimesnsion.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.iris.model.UserMaster;
import com.iris.sdmx.agency.master.entity.AgencyMaster;

/**
 * @author sajadhav
 *
 */
@Table(name = "TBL_SDMX_DIMENSION_MASTER_MOD")
@Entity
public class DimensionMasterMod implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 872329851106330978L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DIM_MASTER_MOD_ID")
	private Long dimMasterModId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DIM_ID_FK")
	private DimensionMaster dimensionmaster;

	@Column(name = "DIM_CODE")
	private String dimCode;

	@Column(name = "DIM_MASTER_JSON")
	private String dimMasterJson;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "ADMIN_STATUS_ID_FK")
	private int adminStatusId;

	@Column(name = "ACTION_ID_FK")
	private int actionId;

	@Column(name = "COMMENTS")
	private String comments;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY_FK")
	private UserMaster lastModifiedBy;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastModifiedOn;

	@Column(name = "LAST_UPDATE_ON")
	private Date lastUpdatedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_APPROVED_BY_FK")
	private UserMaster lastApprovedBy;

	@Column(name = "LAST_APPROVED_ON")
	private Date lastApprovedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGENCY_MASTER_ID_FK")
	private AgencyMaster agencyIdFk;

	@Column(name = "CONCEPT_VERSION")
	private String conceptVersion;

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
	 * @return the dimensionmaster
	 */
	public DimensionMaster getDimensionmaster() {
		return dimensionmaster;
	}

	/**
	 * @param dimensionmaster the dimensionmaster to set
	 */
	public void setDimensionmaster(DimensionMaster dimensionmaster) {
		this.dimensionmaster = dimensionmaster;
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
	 * @return the lastModifiedBy
	 */
	public UserMaster getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
	public void setLastModifiedBy(UserMaster lastModifiedBy) {
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
	public UserMaster getLastApprovedBy() {
		return lastApprovedBy;
	}

	/**
	 * @param lastApprovedBy the lastApprovedBy to set
	 */
	public void setLastApprovedBy(UserMaster lastApprovedBy) {
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
	 * @return the agencyIdFk
	 */
	public AgencyMaster getAgencyIdFk() {
		return agencyIdFk;
	}

	/**
	 * @param agencyIdFk the agencyIdFk to set
	 */
	public void setAgencyIdFk(AgencyMaster agencyIdFk) {
		this.agencyIdFk = agencyIdFk;
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
