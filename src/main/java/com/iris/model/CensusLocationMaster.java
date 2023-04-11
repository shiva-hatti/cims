/**
 * 
 */
package com.iris.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Siddique H Khan
 *
 */

@Entity
@Table(name = "TBL_CENSUS_LOCATION_MASTER")
public class CensusLocationMaster implements Serializable {

	private static final long serialVersionUID = 7551638541452480691L;

	@Id
	@Column(name = "CENSUS_LOCATION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long censusLocationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATE_ID_FK")
	private StateMaster stateId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTRICT_MASTER_ID_FK")
	private StateMaster disctrictId;

	@Column(name = "CENSUS_SUBDISTRICT_CODE")
	private String censusSubdistrictCode;

	@Column(name = "CENSUS_SUBDISTRICT_NAME")
	private String censusSubdistrictName;

	@Column(name = "CENSUS_VILLAGE_CODE")
	private String censusVillageCode;

	@Column(name = "CENSUS_VILLAGE_NAME")
	private String censusVillageName;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster modifiedBy;

	public Long getCensusLocationId() {
		return censusLocationId;
	}

	public void setCensusLocationId(Long censusLocationId) {
		this.censusLocationId = censusLocationId;
	}

	public StateMaster getStateId() {
		return stateId;
	}

	public void setStateId(StateMaster stateId) {
		this.stateId = stateId;
	}

	public StateMaster getDisctrictId() {
		return disctrictId;
	}

	public void setDisctrictId(StateMaster disctrictId) {
		this.disctrictId = disctrictId;
	}

	public String getCensusSubdistrictCode() {
		return censusSubdistrictCode;
	}

	public void setCensusSubdistrictCode(String censusSubdistrictCode) {
		this.censusSubdistrictCode = censusSubdistrictCode;
	}

	public String getCensusSubdistrictName() {
		return censusSubdistrictName;
	}

	public void setCensusSubdistrictName(String censusSubdistrictName) {
		this.censusSubdistrictName = censusSubdistrictName;
	}

	public String getCensusVillageCode() {
		return censusVillageCode;
	}

	public void setCensusVillageCode(String censusVillageCode) {
		this.censusVillageCode = censusVillageCode;
	}

	public String getCensusVillageName() {
		return censusVillageName;
	}

	public void setCensusVillageName(String censusVillageName) {
		this.censusVillageName = censusVillageName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public UserMaster getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public UserMaster getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserMaster modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
