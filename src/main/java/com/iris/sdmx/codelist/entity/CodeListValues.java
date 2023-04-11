/**
 * 
 */
package com.iris.sdmx.codelist.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.model.UserMaster;

/**
 * @author sajadhav
 *
 */
@Entity
@Table(name = "TBL_SDMX_CL_VALUES")
@JsonInclude(Include.NON_NULL)
public class CodeListValues implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9117753196640158993L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CL_VALUES_ID")
	private Long clValueId;

	@Column(name = "CL_VALUE_CODE")
	@Size(min = 1, max = 50)
	private String clValueCode;

	@Column(name = "CL_VALUE_LABLE")
	@Size(min = 1, max = 2000)
	private String clValueLable;

	@Column(name = "CL_VALUE_DESC")
	@Size(min = 1, max = 10000)
	private String clValueDesc;

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

	@Column(name = "LAST_UPDATE_ON")
	private Date lastUpdatedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CL_ID_FK")
	private CodeListMaster codeListMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_CL_VALUES_ID_FK")
	private CodeListValues parentCodeListValues;

	@Transient
	private Long createdOnInLong;

	@Transient
	private Long lastModifiedOnInLong;

	@Transient
	private Long lastUpdatedOnInLong;

	@Transient
	private List<CodeListValues> childCodeListValues = null;

	/**
	 * @return the childCodeListValues
	 */
	public List<CodeListValues> getChildCodeListValues() {
		return childCodeListValues;
	}

	/**
	 * @param childCodeListValues the childCodeListValues to set
	 */
	public void setChildCodeListValues(List<CodeListValues> childCodeListValues) {
		this.childCodeListValues = childCodeListValues;
	}

	/**
	 * @return the createdOnInLOng
	 */
	public Long getCreatedOnInLong() {
		return createdOnInLong;
	}

	/**
	 * @param createdOnInLOng the createdOnInLOng to set
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
	 * @return the clValueId
	 */
	public Long getClValueId() {
		return clValueId;
	}

	/**
	 * @param clValueId the clValueId to set
	 */
	public void setClValueId(Long clValueId) {
		this.clValueId = clValueId;
	}

	/**
	 * @return the clValueCode
	 */
	public String getClValueCode() {
		return clValueCode;
	}

	/**
	 * @param clValueCode the clValueCode to set
	 */
	public void setClValueCode(String clValueCode) {
		this.clValueCode = clValueCode;
	}

	/**
	 * @return the clValueLable
	 */
	public String getClValueLable() {
		return clValueLable;
	}

	/**
	 * @param clValueLable the clValueLable to set
	 */
	public void setClValueLable(String clValueLable) {
		this.clValueLable = clValueLable;
	}

	/**
	 * @return the clValueDesc
	 */
	public String getClValueDesc() {
		return clValueDesc;
	}

	/**
	 * @param clValueDesc the clValueDesc to set
	 */
	public void setClValueDesc(String clValueDesc) {
		this.clValueDesc = clValueDesc;
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
	 * @return the codeListMaster
	 */
	public CodeListMaster getCodeListMaster() {
		return codeListMaster;
	}

	/**
	 * @param codeListMaster the codeListMaster to set
	 */
	public void setCodeListMaster(CodeListMaster codeListMaster) {
		this.codeListMaster = codeListMaster;
	}

	/**
	 * @return the parentCodeListValues
	 */
	public CodeListValues getParentCodeListValues() {
		return parentCodeListValues;
	}

	/**
	 * @param parentCodeListValues the parentCodeListValues to set
	 */
	public void setParentCodeListValues(CodeListValues parentCodeListValues) {
		this.parentCodeListValues = parentCodeListValues;
	}

}
