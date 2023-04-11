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

import com.iris.util.Validations;

/**
 * @author Siddique H Khan
 */
@Entity
@Table(name = "TBL_DISTRICT_MASTER")
public class DistrictMaster implements Serializable {

	private static final long serialVersionUID = 337949588409815684L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "DISTRICT_NAME")
	private String districtName;

	@Column(name = "DISTRICT_CODE")
	private String districtCode;

	@Column(name = "DISTRICT_NAME_BIL")
	private String districtNameBil;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATE_ID_FK")
	private StateMaster stateIdFk;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "CENSUS_DISTRICT_CODE")
	private String censusDistrictCode;

	@Column(name = "LAST_MODIFIED_ON")
	private String lastModifiedOn;

	/**
	 * @return the lastModifiedOn
	 */
	public String getLastModifiedOn() {
		return lastModifiedOn;
	}

	/**
	 * @param lastModifiedOn the lastModifiedOn to set
	 */
	public void setLastModifiedOn(String lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public String getCensusDistrictCode() {
		return censusDistrictCode;
	}

	public void setCensusDistrictCode(String censusDistrictCode) {
		this.censusDistrictCode = censusDistrictCode;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the districtName
	 */
	public String getDistrictName() {
		return districtName;
	}

	/**
	 * @param districtName the districtName to set
	 */
	public void setDistrictName(String districtName) {
		this.districtName = Validations.trimInput(districtName);
	}

	/**
	 * @return the districtCode
	 */
	public String getDistrictCode() {
		return districtCode;
	}

	/**
	 * @param districtCode the districtCode to set
	 */
	public void setDistrictCode(String districtCode) {
		this.districtCode = Validations.trimInput(districtCode);
	}

	/**
	 * @return the districtNameBil
	 */
	public String getDistrictNameBil() {
		return districtNameBil;
	}

	/**
	 * @param districtNameBil the districtNameBil to set
	 */
	public void setDistrictNameBil(String districtNameBil) {
		this.districtNameBil = Validations.trimInput(districtNameBil);
	}

	/**
	 * @return the stateIdFk
	 */
	public StateMaster getStateIdFk() {
		return stateIdFk;
	}

	/**
	 * @param stateIdFk the stateIdFk to set
	 */
	public void setStateIdFk(StateMaster stateIdFk) {
		this.stateIdFk = stateIdFk;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}