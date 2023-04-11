/**
 * 
 */
package com.iris.sdmx.element.entity;

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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.model.UserMaster;

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_ELE_NATURE")
@JsonInclude(Include.NON_NULL)
public class SdmxElementNatureEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NATURE_ID")
	private Long natureId;

	@Column(name = "NATURE_NAME")
	private String natureName;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATED_ON")
	private Date lastUpdatedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFY_BY")
	private UserMaster modifyBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_ON")
	private Date modifyOn;

	/**
	 * @return the natureId
	 */
	public Long getNatureId() {
		return natureId;
	}

	/**
	 * @param natureId the natureId to set
	 */
	public void setNatureId(Long natureId) {
		this.natureId = natureId;
	}

	/**
	 * @return the natureName
	 */
	public String getNatureName() {
		return natureName;
	}

	/**
	 * @param natureName the natureName to set
	 */
	public void setNatureName(String natureName) {
		this.natureName = natureName;
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
	 * @return the modifyBy
	 */
	public UserMaster getModifyBy() {
		return modifyBy;
	}

	/**
	 * @param modifyBy the modifyBy to set
	 */
	public void setModifyBy(UserMaster modifyBy) {
		this.modifyBy = modifyBy;
	}

	/**
	 * @return the modifyOn
	 */
	public Date getModifyOn() {
		return modifyOn;
	}

	/**
	 * @param modifyOn the modifyOn to set
	 */
	public void setModifyOn(Date modifyOn) {
		this.modifyOn = modifyOn;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "SdmxElementNatureEntity [natureId=" + natureId + ", natureName=" + natureName + ", isActive=" + isActive + ", lastUpdatedOn=" + lastUpdatedOn + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifyBy=" + modifyBy + ", modifyOn=" + modifyOn + "]";
	}
}
