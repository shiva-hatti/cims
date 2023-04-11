/**
 * 
 */
package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Siddique H Khan
 *
 */
@Entity
@Table(name = "TBL_REGION_MASTER")
public class RegionMaster implements Serializable {

	private static final long serialVersionUID = -6200599755584191895L;

	@Id
	@Column(name = "REGION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long regionId;

	@Column(name = "REGION_NAME")
	private String regionName;

	@Column(name = "REGION_CODE")
	private String regionCode;

	@Column(name = "REGION_NANE_BIL")
	private String regionNameBil;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

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

	/**
	 * @return the regionId
	 */
	public Long getRegionId() {
		return regionId;
	}

	/**
	 * @param regionId the regionId to set
	 */
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	/**
	 * @return the regionName
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * @param regionName the regionName to set
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	/**
	 * @return the regionCode
	 */
	public String getRegionCode() {
		return regionCode;
	}

	/**
	 * @param regionCode the regionCode to set
	 */
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	/**
	 * @return the regionNameBil
	 */
	public String getRegionNameBil() {
		return regionNameBil;
	}

	/**
	 * @param regionNameBil the regionNameBil to set
	 */
	public void setRegionNameBil(String regionNameBil) {
		this.regionNameBil = regionNameBil;
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
