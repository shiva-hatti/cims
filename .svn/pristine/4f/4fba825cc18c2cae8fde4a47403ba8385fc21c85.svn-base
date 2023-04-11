/**
 * 
 */
package com.iris.sdmx.dimesnsion.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sajadhav
 *
 */
@Table(name = "TBL_SDMX_DIMENSION_TYPE")
@Entity
public class DimensionType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1928408211944341554L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DIMENSION_TYPE_ID")
	private Long dimesnionTypeId;

	@Column(name = "DIMENSION_TYPE_NAME")
	private String dimesnsionTypeName;

	@Column(name = "LAST_UPDATED_ON")
	private Date lastUpdatedOn;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	/**
	 * @return the dimesnionTypeId
	 */
	public Long getDimesnionTypeId() {
		return dimesnionTypeId;
	}

	/**
	 * @param dimesnionTypeId the dimesnionTypeId to set
	 */
	public void setDimesnionTypeId(Long dimesnionTypeId) {
		this.dimesnionTypeId = dimesnionTypeId;
	}

	/**
	 * @return the dimesnsionTypeName
	 */
	public String getDimesnsionTypeName() {
		return dimesnsionTypeName;
	}

	/**
	 * @param dimesnsionTypeName the dimesnsionTypeName to set
	 */
	public void setDimesnsionTypeName(String dimesnsionTypeName) {
		this.dimesnsionTypeName = dimesnsionTypeName;
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

}
