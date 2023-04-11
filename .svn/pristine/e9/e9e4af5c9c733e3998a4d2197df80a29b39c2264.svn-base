/**
 * 
 */
package com.iris.sdmx.codelist.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.model.UserMaster;
import com.iris.sdmx.agency.master.entity.AgencyMaster;

/**
 * @author sajadhav
 *
 */
@Entity
@Table(name = "TBL_SDMX_CL_MASTER")
@JsonInclude(Include.NON_NULL)
public class CodeListMaster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7085678748682586564L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CL_ID")
	private Long clId;

	@Column(name = "CL_CODE")
	@Size(min = 1, max = 50)
	private String clCode;

	@Column(name = "CL_LABLE")
	@Size(min = 1, max = 2000)
	private String clLable;

	@Column(name = "CL_VER")
	private String clVersion;

	@Column(name = "CL_DESC")
	@Size(min = 1, max = 10000)
	private String clDesc;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

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

	@Column(name = "LAST_UPDATED_ON")
	private Date lastUpdatedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_APPROVED_BY_FK")
	private UserMaster lastApprovedBy;

	@Column(name = "LAST_APPROVED_ON")
	private Date lastApprovedOn;

	@OneToMany(mappedBy = "codeListMaster", cascade = CascadeType.ALL)
	@OrderBy("clValueCode")
	private List<CodeListValues> codeListValues;

	@Transient
	private Long createdOnInLong;

	@Transient
	private Long lastModifiedOnInLong;

	@Transient
	private Long lastUpdatedOnInLong;

	@Transient
	private Long lastApprovedOnInLong;

	@Column(name = "IS_PENDING")
	private Boolean isPending;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGENCY_MASTER_ID_FK")
	private AgencyMaster agencyMaster;

	/**
	 * @return the createdOnInLong
	 */
	public Long getCreatedOnInLong() {
		return createdOnInLong;
	}

	/**
	 * @param createdOnInLong the createdOnInLong to set
	 */
	public void setCreatedOnInLong(Long createdOnInLong) {
		this.createdOnInLong = createdOnInLong;
	}

	/**
	 * @return the lastModifiedOnInLong
	 */
	public Long getLastModifiedOnInLong() {
		return lastModifiedOnInLong;
	}

	/**
	 * @param lastModifiedOnInLong the lastModifiedOnInLong to set
	 */
	public void setLastModifiedOnInLong(Long lastModifiedOnInLong) {
		this.lastModifiedOnInLong = lastModifiedOnInLong;
	}

	/**
	 * @return the lastUpdatedOnInLong
	 */
	public Long getLastUpdatedOnInLong() {
		return lastUpdatedOnInLong;
	}

	/**
	 * @param lastUpdatedOnInLong the lastUpdatedOnInLong to set
	 */
	public void setLastUpdatedOnInLong(Long lastUpdatedOnInLong) {
		this.lastUpdatedOnInLong = lastUpdatedOnInLong;
	}

	/**
	 * @return the lastApprovedOnInLong
	 */
	public Long getLastApprovedOnInLong() {
		return lastApprovedOnInLong;
	}

	/**
	 * @param lastApprovedOnInLong the lastApprovedOnInLong to set
	 */
	public void setLastApprovedOnInLong(Long lastApprovedOnInLong) {
		this.lastApprovedOnInLong = lastApprovedOnInLong;
	}

	/**
	 * @return the clId
	 */
	public Long getClId() {
		return clId;
	}

	/**
	 * @param clId the clId to set
	 */
	public void setClId(Long clId) {
		this.clId = clId;
	}

	/**
	 * @return the clCode
	 */
	public String getClCode() {
		return clCode;
	}

	/**
	 * @param clCode the clCode to set
	 */
	public void setClCode(String clCode) {
		this.clCode = clCode;
	}

	/**
	 * @return the clLable
	 */
	public String getClLable() {
		return clLable;
	}

	/**
	 * @param clLable the clLable to set
	 */
	public void setClLable(String clLable) {
		this.clLable = clLable;
	}

	/**
	 * @return the clVersion
	 */
	public String getClVersion() {
		return clVersion;
	}

	/**
	 * @param clVersion the clVersion to set
	 */
	public void setClVersion(String clVersion) {
		this.clVersion = clVersion;
	}

	/**
	 * @return the clDesc
	 */
	public String getClDesc() {
		return clDesc;
	}

	/**
	 * @param clDesc the clDesc to set
	 */
	public void setClDesc(String clDesc) {
		this.clDesc = clDesc;
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
	 * @return the codeListValues
	 */
	public List<CodeListValues> getCodeListValues() {
		return codeListValues;
	}

	/**
	 * @param codeListValues the codeListValues to set
	 */
	public void setCodeListValues(List<CodeListValues> codeListValues) {
		this.codeListValues = codeListValues;
	}

	/**
	 * @return the isPending
	 */
	public Boolean getIsPending() {
		return isPending;
	}

	/**
	 * @param isPending the isPending to set
	 */
	public void setIsPending(Boolean isPending) {
		this.isPending = isPending;
	}

	/**
	 * @return the agencyMaster
	 */
	public AgencyMaster getAgencyMaster() {
		return agencyMaster;
	}

	/**
	 * @param agencyMaster the agencyMaster to set
	 */
	public void setAgencyMaster(AgencyMaster agencyMaster) {
		this.agencyMaster = agencyMaster;
	}

}
